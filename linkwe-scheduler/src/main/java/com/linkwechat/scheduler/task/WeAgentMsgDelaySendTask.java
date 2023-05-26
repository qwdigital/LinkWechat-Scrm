package com.linkwechat.scheduler.task;

import cn.hutool.core.collection.CollectionUtil;
import com.linkwechat.common.enums.QwAppMsgBusinessTypeEnum;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeAgentMsg;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.msg.QwAppMsgBody;
import com.linkwechat.service.IWeAgentMsgService;
import com.linkwechat.service.QwAppSendMsgService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 应用消息定时发送任务
 *
 * @author danmo
 * @date 2022年11月06日 20:30
 */
@Slf4j
@Component
public class WeAgentMsgDelaySendTask {

    @Autowired
    private IWeAgentMsgService weAgentMsgService;

    @Autowired
    private QwAppSendMsgService qwAppSendMsgService;

    @XxlJob("weAgentMsgDelaySendTask")
    public void execute() {
        String jobParam = XxlJobHelper.getJobParam();
        log.info("应用消息定时发送任务 param：{}", jobParam);
        List<WeAgentMsg> waitingList = weAgentMsgService.getWaitingList();
        if (CollectionUtil.isNotEmpty(waitingList)) {
            for (WeAgentMsg msg : waitingList) {
                try {
                    msg.setStatus(2);
                    weAgentMsgService.updateById(msg);
                    QwAppMsgBody body = new QwAppMsgBody();
                    WeMessageTemplate template = new WeMessageTemplate();
                    template.setMsgType(msg.getMsgType());
                    template.setContent(msg.getContent());
                    template.setPicUrl(msg.getPicUrl());
                    template.setDescription(msg.getDescription());
                    template.setFileUrl(msg.getFileUrl());
                    template.setTitle(msg.getTitle());
                    template.setLinkUrl(msg.getLinkUrl());
                    template.setAppId(msg.getAppId());
                    body.setMessageTemplates(template);
                    body.setBusinessType(QwAppMsgBusinessTypeEnum.AGENT.getType());
                    body.setCorpId(SecurityUtils.getCorpId());
                    if (StringUtils.isNotEmpty(msg.getToUser())) {
                        body.setCorpUserIds(Arrays.stream(msg.getToUser().split(",")).collect(Collectors.toList()));
                    }
                    if (StringUtils.isNotEmpty(msg.getToParty())) {
                        body.setDeptIds(Arrays.stream(msg.getToParty().split(",")).collect(Collectors.toList()));
                    }
                    if (StringUtils.isNotEmpty(msg.getToTag())) {
                        body.setTagIds(Arrays.stream(msg.getToTag().split(",")).collect(Collectors.toList()));
                    }
                    body.setCallBackId(msg.getId());
                    qwAppSendMsgService.appMsgSend(body);
                } catch (Exception e) {
                    log.error("应用消息定时发送任务异常：{}",msg.getId(), e);
                }
            }

        }

    }
}
