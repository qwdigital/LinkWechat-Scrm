package com.linkwechat.wecom.factory.impl.customer;

import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.IWeCustomerService;
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


    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        String userId = message.getUserId();
        String externalUserId = message.getExternalUserId();
        weCustomerService.updateCustomerChatStatus(externalUserId);
    }
}