package com.linkwechat.wecom.service.welcome;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeCustomerClient;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeQrAttachments;
import com.linkwechat.wecom.domain.WeQrCode;
import com.linkwechat.wecom.domain.WeTag;
import com.linkwechat.wecom.domain.dto.WeEmpleCodeDto;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.domain.dto.WeWelcomeMsg;
import com.linkwechat.wecom.domain.query.WeCustomerWelcomeQuery;
import com.linkwechat.wecom.domain.vo.WeCommunityWeComeMsgVo;
import com.linkwechat.wecom.domain.vo.WeMakeCustomerTag;
import com.linkwechat.wecom.domain.vo.qr.WeQrCodeDetailVo;
import com.linkwechat.wecom.domain.vo.tag.WeTagVo;
import com.linkwechat.wecom.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 发送欢迎语业务
 * @date 2021/11/13 18:51
 **/
@Service
@Slf4j
public class WelcomeService implements ApplicationListener<WeCustomerWelcomeQuery> {

    @Autowired
    private IWeQrCodeService weQrCodeService;

    @Autowired
    private IWeCommunityNewGroupService weCommunityNewGroupService;

    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private WeCustomerClient weCustomerClient;

    @Autowired
    private IWeMaterialService weMaterialService;

    @Value("${wecome.welcome-msg-default}")
    private String welcomMsgDefault;

    @Override
    @Async
    public void onApplicationEvent(WeCustomerWelcomeQuery query) {
        log.info("开始发送欢迎语：query:{}", JSONObject.toJSONString(query));
        List<WeQrAttachments> qrAttachments = new ArrayList<>();
        if(StringUtils.isNotEmpty(query.getState()) && query.getState().startsWith(WeConstans.WE_QR_CODE_PREFIX)){
            WeQrCode weQrCode = weQrCodeService.getOne(new LambdaQueryWrapper<WeQrCode>()
                    .eq(WeQrCode::getState, query.getState())
                    .eq(WeQrCode::getDelFlag, 0).last("limit 1"));
            if (weQrCode != null) {
                WeQrCodeDetailVo qrDetail = weQrCodeService.getQrDetail(weQrCode.getId());
                qrAttachments.addAll(qrDetail.getQrAttachments());
                makeCustomerTag(query.getExternalUserId(), query.getUserId(), qrDetail.getQrTags());
            } else {
                log.warn("未查询到对应员工活码信息");
            }
        }else  if(StringUtils.isNotEmpty(query.getState()) && query.getState().startsWith(WeConstans.WE_QR_XKLQ_PREFIX)){
            WeCommunityWeComeMsgVo welcomeMsgByState = weCommunityNewGroupService.getWelcomeMsgByState(query.getState());
            if(welcomeMsgByState != null){
                WeQrAttachments textAtt = new WeQrAttachments();
                textAtt.setMsgType(MessageType.TEXT.getMessageType());
                textAtt.setContent(welcomeMsgByState.getWelcomeMsg());
                qrAttachments.add(textAtt);
                WeQrAttachments imageAtt = new WeQrAttachments();
                imageAtt.setMsgType(MessageType.IMAGE.getMessageType());
                imageAtt.setMediaId(welcomeMsgByState.getCodeUrl());
                qrAttachments.add(imageAtt);
                makeCustomerTag(query.getExternalUserId(), query.getUserId(), welcomeMsgByState.getTagList());
            }
        }
        WeResultDto resultDto = sendWelcomeMsg(query, qrAttachments);
        log.info("结束发送欢迎语：result:{}", JSONObject.toJSONString(resultDto));
    }

    /**
     * 客户打标签
     *
     * @param externaUserId 客户id
     * @param userId        员工id
     * @param qrTags        标签id
     */
    private void makeCustomerTag(String externaUserId, String userId, List<WeTagVo> qrTags) {
        if (CollectionUtil.isNotEmpty(qrTags)) {
            List<WeTag> weTagList = qrTags.stream().map(tag -> {
                WeTag weTag = new WeTag();
                weTag.setName(tag.getTagName());
                weTag.setTagId(tag.getTagId());
                return weTag;
            }).collect(Collectors.toList());
            WeMakeCustomerTag makeCustomerTag = new WeMakeCustomerTag();
            makeCustomerTag.setExternalUserid(externaUserId);
            makeCustomerTag.setUserId(userId);
            makeCustomerTag.setAddTag(weTagList);
            try {
                weCustomerService.makeLabel(makeCustomerTag);
            } catch (Exception e) {
                log.info("发送欢迎语客户打标签失败 ex:{}", e);
            }
        }
    }

    /**
     * 发送欢迎语
     *
     * @param query    客户信息
     * @param qrAttachments 素材
     * @return
     */
    private WeResultDto sendWelcomeMsg(WeCustomerWelcomeQuery query, List<WeQrAttachments> qrAttachments) {
        String welcomText = welcomMsgDefault;
        WeWelcomeMsg welcomeMsg = WeWelcomeMsg.builder().welcome_code(query.getWelcomeCode()).build();
        if(CollectionUtil.isNotEmpty(qrAttachments)){
            WeQrAttachments weQrAttachments = qrAttachments.stream()
                    .filter(qrAttachment -> ObjectUtil.equal(MessageType.TEXT.getMessageType(), qrAttachment.getMsgType()))
                    .findFirst().orElseGet(null);
            if(weQrAttachments != null){
                welcomText = weQrAttachments.getContent();
            }
            getMediaId(qrAttachments);
            welcomeMsg.setAttachments(qrAttachments);
        }
        WeCustomer weCustomer = weCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getFirstUserId,query.getUserId())
                .eq(WeCustomer::getExternalUserid,query.getExternalUserId()).eq(WeCustomer::getDelFlag,0).last("limit 1"));
        if(weCustomer != null){
            String customerName = weCustomer.getCustomerName();
            welcomText = welcomText.replaceAll("#客户昵称#",customerName);
        }
        welcomeMsg.setText(WeWelcomeMsg.Text.builder().content(welcomText).build());
        return weCustomerClient.sendWelcomeMsg(welcomeMsg);
    }

    void getMediaId(List<WeQrAttachments> messageTemplates) {
        Optional.ofNullable(messageTemplates).orElseGet(ArrayList::new).forEach(messageTemplate -> {
            if (ObjectUtil.equal(MessageType.IMAGE.getMessageType(), messageTemplate.getMsgType())) {
                WeMediaDto weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplate.getMediaId()
                        , MessageType.IMAGE.getMessageType()
                        , FileUtil.getName(messageTemplate.getMediaId()));
                messageTemplate.setMediaId(weMedia.getMedia_id());
            } else if (ObjectUtil.equal(MessageType.MINIPROGRAM.getMessageType(), messageTemplate.getMsgType())) {
                WeMediaDto weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplate.getMediaId()
                        , MessageType.IMAGE.getMessageType()
                        , FileUtil.getName(messageTemplate.getMediaId()));
                messageTemplate.setMediaId(weMedia.getMedia_id());
            } else if (ObjectUtil.equal(MessageType.VIDEO.getMessageType(), messageTemplate.getMsgType())) {
                WeMediaDto weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplate.getMediaId()
                        , MessageType.IMAGE.getMessageType()
                        , FileUtil.getName(messageTemplate.getMediaId()));
                messageTemplate.setMediaId(weMedia.getMedia_id());
            } else if (ObjectUtil.equal(MessageType.FILE.getMessageType(), messageTemplate.getMsgType())) {
                WeMediaDto weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplate.getMediaId()
                        , MessageType.IMAGE.getMessageType()
                        , FileUtil.getName(messageTemplate.getMediaId()));
                messageTemplate.setMediaId(weMedia.getMedia_id());
            }
        });
    }
}
