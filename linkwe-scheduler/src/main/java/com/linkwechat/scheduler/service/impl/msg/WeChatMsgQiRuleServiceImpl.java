package com.linkwechat.scheduler.service.impl.msg;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.domain.WeAgentMsg;
import com.linkwechat.domain.WeQiRuleMsg;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.msg.QwAppMsgBody;
import com.linkwechat.domain.wecom.query.msg.WeAppMsgQuery;
import com.linkwechat.domain.wecom.vo.msg.WeAppMsgVo;
import com.linkwechat.scheduler.service.AbstractAppMsgService;
import com.linkwechat.service.IWeQiRuleMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * @author danmo
 * @description 质检通知应用消息
 * @date 2023/05/06 23:45
 **/
@Service("WeChatMsgQiRuleService")
@Slf4j
public class WeChatMsgQiRuleServiceImpl extends AbstractAppMsgService {

    private Long callBackId;

    @Autowired
    private IWeQiRuleMsgService weQiRuleMsgService;

    @Override
    protected WeAppMsgQuery getWeAppMsg(QwAppMsgBody appMsgBody) {
        WeAppMsgQuery query = new WeAppMsgQuery();
        query.setCorpid(appMsgBody.getCorpId());
        if(CollectionUtil.isNotEmpty(appMsgBody.getCorpUserIds())){
            query.setTouser(String.join("|", appMsgBody.getCorpUserIds()));
        }else if(CollectionUtil.isNotEmpty(appMsgBody.getDeptIds())){
            query.setToparty(String.join("|", appMsgBody.getDeptIds()));
        }else if(CollectionUtil.isNotEmpty(appMsgBody.getTagIds())){
            query.setTotag(String.join("|", appMsgBody.getTagIds()));
        }
        WeMessageTemplate messageTemplates = appMsgBody.getMessageTemplates();
        if(MessageType.TEXT.getMessageType().equals(messageTemplates.getMsgType())){
            query.setMsgtype(MessageType.TEXT.getMessageType());
            query.setText(WeAppMsgQuery.Text.builder().content(messageTemplates.getContent()).build());
        }else if(MessageType.TEXTCARD.getMessageType().equals(messageTemplates.getMsgType())){
            query.setMsgtype(MessageType.TEXTCARD.getMessageType());
            WeAppMsgQuery.TextCard textCard = WeAppMsgQuery.TextCard.builder()
                    .title(messageTemplates.getTitle())
                    .description(messageTemplates.getDescription())
                    .url(messageTemplates.getLinkUrl())
                    .btntxt(messageTemplates.getBtntxt())
                    .build();
            query.setTextcard(textCard);
        }
        return query;
    }

    @Override
    protected void callBackResult(WeAppMsgVo appMsgVo) {
        log.info(">>>>>>【质检通知任务发送结果】 callBackId:{} ,appMsgVo:{}", callBackId, JSONObject.toJSONString(appMsgVo));
        WeQiRuleMsg weQiRuleMsg = new WeQiRuleMsg();
        weQiRuleMsg.setId(callBackId);
        if (appMsgVo.getErrMsg() != null && !Objects.equals(0,appMsgVo.getErrCode())) {
            weQiRuleMsg.setStatus(2);
        } else {
            weQiRuleMsg.setStatus(1);
        }
        weQiRuleMsgService.updateById(weQiRuleMsg);
    }
}
