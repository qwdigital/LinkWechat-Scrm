package com.linkwechat.scheduler.service.impl.welcome;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.qr.WeQrAttachments;
import com.linkwechat.domain.qr.vo.WeLxQrCodeDetailVo;
import com.linkwechat.domain.wecom.callback.WeBackCustomerVo;
import com.linkwechat.service.IWeLxQrCodeService;
import com.linkwechat.service.IWeQrAttachmentsService;
import com.linkwechat.service.IWeQrTagRelService;
import com.linkwechat.service.IWeRedEnvelopesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 拉新业务欢迎语消息
 *
 * @author danmo
 * @date 2023年03月10日 14:38
 */
@Slf4j
@Service
public class WeLxQrCodeMsgServiceImpl extends AbstractWelcomeMsgServiceImpl {

    @Autowired
    private IWeLxQrCodeService weLxQrCodeService;

    @Autowired
    private IWeQrAttachmentsService weQrAttachmentsService;

    @Autowired
    private IWeQrTagRelService weQrTagRelService;

    @Autowired
    private IWeRedEnvelopesService weRedEnvelopesService;

    //拉新红包图片地址
    @Value("${welcome-msg.lxqr.red-packet-img:}")
    private String redPacketImg;

    //拉新卡券h5地址
    @Value("${welcome-msg.lxqr.red-packet-link:}")
    private String redPacketLink;

    //拉新卡券图片地址
    @Value("${welcome-msg.lxqr.coupon-img:}")
    private String coupontImg;

    //拉新卡券h5地址
    @Value("${welcome-msg.lxqr.coupon-link:}")
    private String coupontLink;

    //拉新描述字段
    @Value("${welcome-msg.lxqr.description:}")
    private String description;


    @Override
    public void msgHandle(WeBackCustomerVo query) {

        log.info("红包卡券拉新欢迎语消息 query：{}", JSONObject.toJSONString(query));

        List<WeMessageTemplate> templates = new ArrayList<>();

        WeLxQrCodeDetailVo qrDetail = weLxQrCodeService.getQrDetailByState(query.getState());

        if (Objects.nonNull(qrDetail)) {

            List<WeQrAttachments> qrAttachments = qrDetail.getQrAttachments();

            List<WeMessageTemplate> templateList = qrAttachments.stream().map(qrAttachment -> {
                WeMessageTemplate template = new WeMessageTemplate();
                template.setMsgType(qrAttachment.getMsgType());
                template.setContent(qrAttachment.getContent());
                template.setMediaId(qrAttachment.getMediaId());
                template.setTitle(qrAttachment.getTitle());
                template.setDescription(qrAttachment.getDescription());
                template.setAppId(qrAttachment.getAppId());
                template.setFileUrl(qrAttachment.getFileUrl());
                template.setPicUrl(qrAttachment.getPicUrl());
                template.setLinkUrl(qrAttachment.getLinkUrl());
                template.setMaterialId(qrAttachment.getMaterialId());
                return template;
            }).collect(Collectors.toList());

            templates.addAll(templateList);

            WeMessageTemplate template = new WeMessageTemplate();
            template.setMsgType(MediaType.LINK.getMediaType());
            template.setDescription(description);
            template.setTitle(qrDetail.getName());

            if (ObjectUtil.equal(1, qrDetail.getType())) {
                String orderNo = "0";
                try {
                    //发送客户红包
                    String businessData = qrDetail.getBusinessData();
                    JSONObject redInfo = JSONObject.parseObject(businessData);
                    String money = redInfo.getString("money");
                    String name = redInfo.getString("name");
                    int redEnvelopeAmount = new BigDecimal(money).multiply(BigDecimal.valueOf(100)).intValue();
                    orderNo = weRedEnvelopesService.createCustomerRedEnvelopesOrder(String.valueOf(qrDetail.getBusinessId()), redEnvelopeAmount, name, 1, query.getUserID(), 1, query.getExternalUserID());
                } catch (Exception e) {
                    log.error("发送客户红包失败 query:{}",JSONObject.toJSONString(query),e);
                }
                template.setPicUrl(redPacketImg);
                template.setLinkUrl(StringUtils.format(redPacketLink, qrDetail.getId(),qrDetail.getType(),orderNo));
                templates.add(template);
            }else if (ObjectUtil.equal(2, qrDetail.getType())) {
                template.setPicUrl(redPacketImg);
                template.setLinkUrl(StringUtils.format(coupontLink, qrDetail.getId(),qrDetail.getType()));
                templates.add(template);
            }

            makeCustomerTag(query.getExternalUserID(), query.getUserID(), qrDetail.getQrTags());
        }

        sendWelcomeMsg(query, templates);

    }
}
