package com.linkwechat.scheduler.service.impl.welcome;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.domain.WeCustomerLink;
import com.linkwechat.domain.WeCustomerLinkAttachments;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.qr.WeQrCode;
import com.linkwechat.domain.wecom.callback.WeBackCustomerVo;
import com.linkwechat.service.IWeCustomerLinkAttachmentsService;
import com.linkwechat.service.IWeCustomerLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 获客助手欢迎语
 */
@Slf4j
@Service
public class WeCustomerLinkMsgServiceImpl  extends AbstractWelcomeMsgServiceImpl{


    @Autowired
    private IWeCustomerLinkService iWeCustomerLinkService;


    @Autowired
    private IWeCustomerLinkAttachmentsService iWeCustomerLinkAttachmentsService;


    @Override
    public void msgHandle(WeBackCustomerVo query) {
        log.info("获客助手欢迎语 query：{}", JSONObject.toJSONString(query));
        List<WeMessageTemplate> templates = new ArrayList<>();

        WeCustomerLink weCustomerLink = iWeCustomerLinkService.getOne(new LambdaQueryWrapper<WeCustomerLink>()
                .eq(WeCustomerLink::getState, query.getState())
                .eq(WeCustomerLink::getDelFlag, Constants.COMMON_STATE).last("limit 1"));

        if (Objects.nonNull(weCustomerLink)) {
            List<WeCustomerLinkAttachments> weCustomerLinkAttachments = iWeCustomerLinkAttachmentsService.list(new LambdaQueryWrapper<WeCustomerLinkAttachments>()
                    .eq(WeCustomerLinkAttachments::getCustomerLinkId, weCustomerLink.getId()));


            if(CollectionUtil.isNotEmpty(weCustomerLinkAttachments)){

                List<WeMessageTemplate> templateList = weCustomerLinkAttachments.stream().map(qrAttachment -> {
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


            }else {
                log.info("未查询到对应的获客链接信息");
            }

            sendWelcomeMsg(query, templates);


        }



    }
}
