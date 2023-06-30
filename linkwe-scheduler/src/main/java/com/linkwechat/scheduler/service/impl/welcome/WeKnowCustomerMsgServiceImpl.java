package com.linkwechat.scheduler.service.impl.welcome;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.domain.know.WeKnowCustomerAttachments;
import com.linkwechat.domain.know.WeKnowCustomerCode;
import com.linkwechat.domain.know.WeKnowCustomerCodeTag;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.tag.vo.WeTagVo;
import com.linkwechat.domain.wecom.callback.WeBackCustomerVo;
import com.linkwechat.service.IWeKnowCustomerAttachmentsService;
import com.linkwechat.service.IWeKnowCustomerCodeService;
import com.linkwechat.service.IWeKnowCustomerCodeTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 识客码欢迎语消息
 *
 * @author danmo
 * @date 2023年03月10日 14:38
 */
@Slf4j
@Service
public class WeKnowCustomerMsgServiceImpl extends AbstractWelcomeMsgServiceImpl {

    @Autowired
    private IWeKnowCustomerCodeService iWeKnowCustomerCodeService;

    @Autowired
    private IWeKnowCustomerAttachmentsService iWeKnowCustomerAttachmentsService;

    @Autowired
    private IWeKnowCustomerCodeTagService iWeKnowCustomerCodeTagService;

    @Override
    public void msgHandle(WeBackCustomerVo query) {

        log.info("识客码欢迎语消息 query：{}", JSONObject.toJSONString(query));

        List<WeMessageTemplate> templates = new ArrayList<>();

        WeKnowCustomerCode weKnowCustomerCode = iWeKnowCustomerCodeService.getOne(new LambdaQueryWrapper<WeKnowCustomerCode>()
                .eq(WeKnowCustomerCode::getAddWeUserState, query.getState()).last("limit 1"));
        if (weKnowCustomerCode != null) {
            List<WeKnowCustomerAttachments> customerAttachments =
                    iWeKnowCustomerAttachmentsService.list(new LambdaQueryWrapper<WeKnowCustomerAttachments>()
                            .eq(WeKnowCustomerAttachments::getKnowCustomerId, weKnowCustomerCode.getId()));
            if (CollectionUtil.isNotEmpty(customerAttachments)) {
                List<WeMessageTemplate> templateList = customerAttachments.stream().map(qrAttachment -> {
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

                //获取并打标签
                List<WeKnowCustomerCodeTag> weKnowCustomerCodeTags = iWeKnowCustomerCodeTagService.list(new LambdaQueryWrapper<WeKnowCustomerCodeTag>()
                        .eq(WeKnowCustomerCodeTag::getKnowCustomerCodeId, weKnowCustomerCode.getId()));
                if (CollectionUtil.isNotEmpty(weKnowCustomerCodeTags)) {
                    makeCustomerTag(query.getExternalUserID(), query.getUserID(),
                            weKnowCustomerCodeTags.stream().map(v -> {
                                return new WeTagVo( v.getTagId(),v.getTagName());
                            }).collect(Collectors.toList())
                    );
                }
            }
        }

        sendWelcomeMsg(query, templates);
    }
}
