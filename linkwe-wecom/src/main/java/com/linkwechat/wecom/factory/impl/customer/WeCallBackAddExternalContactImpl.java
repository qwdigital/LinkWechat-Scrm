package com.linkwechat.wecom.factory.impl.customer;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeCustomerClient;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.WeEmpleCodeDto;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import com.linkwechat.wecom.domain.dto.WeWelcomeMsg;
import com.linkwechat.wecom.domain.dto.customer.CutomerTagEdit;
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
 * @description 新增客户事件
 * @date 2021/1/20 23:18
 **/
@Slf4j
@Component("add_external_contact")
public class WeCallBackAddExternalContactImpl extends WeEventStrategy {
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
    @Autowired
    private IWeTaskFissionRecordService weTaskFissionRecordService;
    @Autowired
    private IWeTaskFissionRewardService weTaskFissionRewardService;
    @Autowired
    private IWeTaskFissionService weTaskFissionService;

    private ThreadLocal<WeFlowerCustomerRel> weFlowerCustomerRelThreadLocal = new ThreadLocal<>();

    @Autowired
    private IWeGroupCodeService weGroupCodeService;


    @Autowired
    private WeCustomerClient weCustomerClient;


    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        if (message.getExternalUserId() != null) {
            weCustomerService.getCustomersInfoAndSynchWeCustomer(message.getExternalUserId(),message.getUserId());
        }


        if (message.getState() != null && message.getWelcomeCode() != null) {
            if (isFission(message.getState())) {
                taskFissionRecordHandle(message.getState(), message.getWelcomeCode(), message.getUserId(), message.getExternalUserId());
            } else {
                empleCodeHandle(message.getState(), message.getWelcomeCode(), message.getUserId(), message.getExternalUserId());
            }
        }

    }

    //裂变任务处理
    private void taskFissionRecordHandle(String state, String wecomCode, String userId, String externalUserId) {
        log.info("裂变任务处理  >>>>>>>>>>start");
        //查询裂变客户任务记录
        String fissionRecordId = state.substring(WeConstans.FISSION_PREFIX.length());
        WeTaskFissionRecord weTaskFissionRecord = weTaskFissionRecordService.selectWeTaskFissionRecordById(Long.valueOf(fissionRecordId));
        if (weTaskFissionRecord != null) {
            //查询裂变任务详情
            WeTaskFission weTaskFission = weTaskFissionService
                    .selectWeTaskFissionById(weTaskFissionRecord.getTaskFissionId());
            Long fissNum = weTaskFissionRecord.getFissNum();
            if (weFlowerCustomerRelThreadLocal.get() == null){
                fissNum++;
                weTaskFissionRecord.setFissNum(fissNum);
            }
            log.info("查询裂变任务详情  >>>>>>>>>>{}",JSONObject.toJSONString(weTaskFissionRecord));
            if (weTaskFission != null){
                //发送欢迎语
                WeWelcomeMsg.WeWelcomeMsgBuilder weWelcomeMsgBuilder = WeWelcomeMsg.builder().welcome_code(wecomCode);
                weWelcomeMsgBuilder.text(WeWelcomeMsg.Text.builder()
                        .content(weTaskFission.getWelcomeMsg()).build());
                weCustomerService.sendWelcomeMsg(weWelcomeMsgBuilder.build());
            }

            //裂变数量完成任务处理,消费兑换码
            if (fissNum >= weTaskFission.getFissNum()){
                log.info("裂变数量完成任务处理,消费兑换码  >>>>>>>>>>{}",fissNum);
                weTaskFissionRecord.setCompleteTime(new Date());
                WeTaskFissionReward reward = new WeTaskFissionReward();
                reward.setTaskFissionId(weTaskFissionRecord.getTaskFissionId());
                reward.setRewardCodeStatus(0);
                List<WeTaskFissionReward> weTaskFissionRewardList = weTaskFissionRewardService.selectWeTaskFissionRewardList(reward);
                WeTaskFissionReward fissionReward = weTaskFissionRewardList.get(0);
                fissionReward.setRewardUser(weTaskFissionRecord.getCustomerName());
                fissionReward.setRewardUserId(weTaskFissionRecord.getCustomerId());
                weTaskFissionRewardService.updateWeTaskFissionReward(fissionReward);
            }
            log.info("裂变任务处理变更  >>>>>>>>>>{}",JSONObject.toJSONString(weTaskFissionRecord));
            weTaskFissionRecordService.updateWeTaskFissionRecord(weTaskFissionRecord);
            log.info("裂变任务处理  >>>>>>>>>>end");
        }
    }

    /**
     * 活码欢迎语发送
     *
     * @param state          渠道
     * @param wecomCode      欢迎语code
     * @param userId         成员id
     * @param externalUserId 客户id
     */
    private void empleCodeHandle(String state, String wecomCode, String userId, String externalUserId) {
        if(StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(externalUserId)){
            try {

                log.info("执行发送欢迎语>>>>>>>>>>>>>>>");
                WeWelcomeMsg.WeWelcomeMsgBuilder weWelcomeMsgBuilder = WeWelcomeMsg.builder().welcome_code(wecomCode);
                WeEmpleCodeDto messageMap = weEmpleCodeService.selectWelcomeMsgByState(state);
                if (StringUtils.isNotNull(messageMap)) {
                    //查询活码对应标签
                    List<WeEmpleCodeTag> tagList = weEmpleCodeTagService.list(new LambdaQueryWrapper<WeEmpleCodeTag>()
                            .eq(WeEmpleCodeTag::getEmpleCodeId, messageMap.getEmpleCodeId()));
                    if(CollectionUtil.isNotEmpty(tagList)){

                        List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels = new ArrayList<>();

                        tagList.stream().forEach(k->{
                            weFlowerCustomerTagRels.add(
                                    WeFlowerCustomerTagRel.builder()
                                            .tagId(k.getTagId())
                                            .externalUserid(externalUserId)
                                            .userId(userId)
                                            .createTime(new Date())
                                            .build()
                            );
                        });

                            weFlowerCustomerTagRelService.batchAddOrUpdate(weFlowerCustomerTagRels);

                            //标签同步到企业微信端
                            weCustomerClient.makeCustomerLabel(CutomerTagEdit.builder()
                                    .external_userid(externalUserId)
                                    .userid(userId)
                                    .add_tag(weFlowerCustomerTagRels.stream()
                                            .map(WeFlowerCustomerTagRel::getTagId).toArray(String[]::new))
                                    .build());

                    }



                    // 发送欢迎语
                    log.debug(">>>>>>>>>欢迎语查询结果：{}", JSONObject.toJSONString(messageMap));
                    // 设定欢迎语文字
                    if (StringUtils.isNotEmpty(messageMap.getWelcomeMsg())) {
                        weWelcomeMsgBuilder.text(WeWelcomeMsg.Text.builder().content(messageMap.getWelcomeMsg()).build());
                    }
                    // 设置欢迎语图片
                    // 新客拉群创建的员工活码欢迎语图片(群活码图片)
                    String codeUrl = weGroupCodeService.selectGroupCodeUrlByEmplCodeState(state);
                    if (StringUtils.isNotNull(codeUrl)) {
                        buildWelcomeMsgImg(weWelcomeMsgBuilder, codeUrl, FileUtil.getName(codeUrl));
                    }else{

                        WeEmpleCode weEmpleCode = weEmpleCodeService.selectWeEmpleCodeById(Long.valueOf(state));
                        if(null != weEmpleCode){
                            if(weEmpleCode.getWeMaterial() !=null && StringUtils.isNotEmpty(weEmpleCode.getWeMaterial().getMaterialUrl())){
                                buildWelcomeMsgImg(weWelcomeMsgBuilder, weEmpleCode.getWeMaterial().getMaterialUrl(),
                                        weEmpleCode.getWeMaterial().getMaterialName());
                            }
                        }
                    }

                    weCustomerService.sendWelcomeMsg(weWelcomeMsgBuilder.build());
                }

            } catch (Exception e) {
                log.error("执行发送欢迎语失败！", e.getMessage());
            }
        }
    }

    private boolean isFission(String str) {
        return str.contains(WeConstans.FISSION_PREFIX);
    }

    /**
     * 构建欢迎语的图片部分
     *
     * @param builder  欢迎语builder
     * @param picUrl   图片链接
     * @param fileName 图片名称
     */
    private void buildWelcomeMsgImg(WeWelcomeMsg.WeWelcomeMsgBuilder builder, String picUrl, String fileName) {
        WeMediaDto weMediaDto = weMaterialService.uploadTemporaryMaterial(picUrl, MediaType.IMAGE.getMediaType(), fileName);
        Optional.ofNullable(weMediaDto).ifPresent(media -> {
            builder.image(WeWelcomeMsg.Image.builder().media_id(media.getMedia_id()).pic_url(media.getUrl()).build());
        });
    }
}
