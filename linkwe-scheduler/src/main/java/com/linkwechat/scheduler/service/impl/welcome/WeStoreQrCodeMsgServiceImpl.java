package com.linkwechat.scheduler.service.impl.welcome;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.qr.WeQrAttachments;
import com.linkwechat.domain.storecode.entity.WeStoreCodeConfig;
import com.linkwechat.domain.tag.vo.WeTagVo;
import com.linkwechat.domain.wecom.callback.WeBackCustomerVo;
import com.linkwechat.service.IWeQrAttachmentsService;
import com.linkwechat.service.IWeStoreCodeConfigService;
import com.linkwechat.service.IWeTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 门店导购欢迎语消息
 *
 * @author danmo
 * @date 2023年03月10日 14:38
 */
@Slf4j
@Service
public class WeStoreQrCodeMsgServiceImpl extends AbstractWelcomeMsgServiceImpl {

    @Autowired
    private IWeStoreCodeConfigService iWeStoreCodeConfigService;

    @Autowired
    private IWeQrAttachmentsService attachmentsService;

    @Autowired
    private IWeTagService iWeTagService;

    @Override
    public void msgHandle(WeBackCustomerVo query) {

        log.info("门店导购欢迎语语消息 query：{}", JSONObject.toJSONString(query));

        List<WeMessageTemplate> templates = new ArrayList<>();

        WeStoreCodeConfig storeCodeConfig = iWeStoreCodeConfigService.getOne(new LambdaQueryWrapper<WeStoreCodeConfig>()
                .eq(WeStoreCodeConfig::getState, query.getState()));

        if (null != storeCodeConfig) {
            List<WeQrAttachments> weQrAttachments = attachmentsService.list(new LambdaQueryWrapper<WeQrAttachments>()
                    .eq(WeQrAttachments::getQrId, storeCodeConfig.getId())
                    .eq(WeQrAttachments::getBusinessType, 2));

            if (CollectionUtil.isNotEmpty(weQrAttachments)) {

                List<WeMessageTemplate> templateList = weQrAttachments.stream().map(qrAttachment -> {
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
                String tagIds = storeCodeConfig.getTagIds();
                if (StringUtils.isNotEmpty(tagIds)) {
                    List<WeTag> weTags = iWeTagService.list(new LambdaQueryWrapper<WeTag>()
                            .in(WeTag::getTagId, ListUtil.toList(tagIds.split(","))));
                    if (CollectionUtil.isNotEmpty(weTags)) {
                        makeCustomerTag(query.getExternalUserID(), query.getUserID(),
                                weTags.stream().map(v -> {
                                    return new WeTagVo(v.getName(), v.getTagId());
                                }).collect(Collectors.toList())
                        );
                    }
                }
            }
        }

        sendWelcomeMsg(query, templates);

    }
}
