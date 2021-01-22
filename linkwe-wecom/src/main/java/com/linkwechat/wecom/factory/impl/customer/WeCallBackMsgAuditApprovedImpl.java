package com.linkwechat.wecom.factory.impl.customer;

import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.wecom.client.WeMessagePushClient;
import com.linkwechat.wecom.domain.WeChatContactMapping;
import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 客户同意进行聊天内容存档事件回调
 * @date 2021/1/21 1:24
 **/
@Slf4j
@Component("msg_audit_approved")
public class WeCallBackMsgAuditApprovedImpl extends WeEventStrategy {
    @Autowired
    private IWeCustomerService weCustomerService;
    @Autowired
    private IWeChatContactMappingService weChatContactMappingService;

    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        String userId = message.getUserId();
        String externalUserId = message.getExternalUserId();
        WeChatContactMapping fromMapping = new WeChatContactMapping();
        fromMapping.setFromId(userId);
        fromMapping.setReceiveId(externalUserId);
        fromMapping.setIsCustom(WeConstans.ID_TYPE_EX);
        weChatContactMappingService.insertWeChatContactMapping(fromMapping);
        WeChatContactMapping receiveMapping = new WeChatContactMapping();
        receiveMapping.setFromId(externalUserId);
        receiveMapping.setReceiveId(userId);
        receiveMapping.setIsCustom(WeConstans.ID_TYPE_USER);
        weChatContactMappingService.insertWeChatContactMapping(receiveMapping);

        weCustomerService.updateCustomerChatStatus(externalUserId);
    }
}