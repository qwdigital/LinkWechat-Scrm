package com.linkwechat.wecom.factory.impl.customer;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeEmpleCodeTag;
import com.linkwechat.wecom.domain.WeFlowerCustomerRel;
import com.linkwechat.wecom.domain.WeFlowerCustomerTagRel;
import com.linkwechat.wecom.domain.dto.WeEmpleCodeDto;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import com.linkwechat.wecom.domain.dto.WeWelcomeMsg;
import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author danmo
 * @description 外部联系人免验证添加成员事件
 * @date 2021/1/20 23:28
 **/
@Slf4j
@Component("add_half_external_contact")
public class WeCallBackAddHalfExternalContactImpl extends WeEventStrategy {

    @Autowired
    private IWeCustomerService weCustomerService;
    @Autowired
    private IWeEmpleCodeTagService weEmpleCodeTagService;
    @Autowired
    private IWeEmpleCodeService weEmpleCodeService;

    @Autowired
    private IWeFlowerCustomerTagRelService weFlowerCustomerTagRelService;

    @Autowired
    private IWeMaterialService weMaterialService;

    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        try {
            if (message.getExternalUserId() != null) {
                weCustomerService.getCustomersInfoAndSynchWeCustomer(message.getExternalUserId(),message.getUserId());
            }
            //向扫码客户发送欢迎语
            if (message.getState() != null && message.getWelcomeCode() != null) {


                log.info("执行发送欢迎语>>>>>>>>>>>>>>>");
                WeWelcomeMsg.WeWelcomeMsgBuilder weWelcomeMsgBuilder = WeWelcomeMsg.builder().welcome_code(message.getWelcomeCode());
                WeEmpleCodeDto messageMap = weEmpleCodeService.selectWelcomeMsgByScenario(message.getState(),message.getUserId());
                String empleCodeId = messageMap.getEmpleCodeId();
                //查询活码对应标签
                List<WeEmpleCodeTag> tagList = weEmpleCodeTagService.list(new LambdaQueryWrapper<WeEmpleCodeTag>()
                        .eq(WeEmpleCodeTag::getEmpleCodeId, empleCodeId));
                //客户添加活码标签
                if(CollectionUtil.isNotEmpty(tagList)){
                    List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels = new ArrayList<>();
                    tagList.stream().forEach(k->{
                        weFlowerCustomerTagRels.add(
                                WeFlowerCustomerTagRel.builder()
                                        .tagId(k.getTagId())
                                        .externalUserid( message.getUserId())
                                        .userId(message.getExternalUserId())
                                        .createTime(new Date())
                                        .build()
                        );

                    });
                    weFlowerCustomerTagRelService.batchAddOrUpdate(weFlowerCustomerTagRels);
                }



                log.debug(">>>>>>>>>欢迎语查询结果：{}", JSONObject.toJSONString(messageMap));
                if (messageMap != null) {
                    if (StringUtils.isNotEmpty(messageMap.getWelcomeMsg())){
                        weWelcomeMsgBuilder.text(WeWelcomeMsg.Text.builder()
                                .content(messageMap.getWelcomeMsg()).build());
                    }
                    if(StringUtils.isNotEmpty(messageMap.getCategoryId())){
                        WeMediaDto weMediaDto = weMaterialService
                                .uploadTemporaryMaterial(messageMap.getMaterialUrl(),messageMap.getMaterialName(), MediaType.IMAGE.getMediaType());
                        Optional.ofNullable(weMediaDto).ifPresent(media ->{
                            weWelcomeMsgBuilder.image(WeWelcomeMsg.Image.builder().media_id(media.getMedia_id())
                                    .pic_url(media.getUrl()).build());
                        });
                    }
                    weCustomerService.sendWelcomeMsg(weWelcomeMsgBuilder.build());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("执行发送欢迎语失败！",e);
        }
    }
}
