package com.linkwechat.wecom.factory.impl.customer;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.wecom.client.WeMessagePushClient;
import com.linkwechat.wecom.domain.WeCorpAccount;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.dto.WeMessagePushDto;
import com.linkwechat.wecom.domain.dto.message.TextMessageDto;
import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import com.linkwechat.wecom.service.IWeCustomerService;
import com.linkwechat.wecom.service.IWeFlowerCustomerRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 删除跟进成员事件
 * @date 2021/1/20 23:36
 **/
@Slf4j
@Component("del_follow_user")
public class WeCallBackDelFollowUserImpl extends WeEventStrategy {
    @Autowired
    private IWeCustomerService weCustomerService;
    @Autowired
    private IWeFlowerCustomerRelService weFlowerCustomerRelService;
    @Autowired
    private IWeCorpAccountService weCorpAccountService;
    @Autowired
    private WeMessagePushClient weMessagePushClient;

    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        try {
            if (message.getUserId() != null && message.getExternalUserId() != null) {
                weFlowerCustomerRelService.deleteFollowUser(message.getUserId(), message.getExternalUserId());
                WeCorpAccount validWeCorpAccount = weCorpAccountService.findValidWeCorpAccount();
                Optional.ofNullable(validWeCorpAccount).ifPresent(weCorpAccount -> {
                    String customerChurnNoticeSwitch = weCorpAccount.getCustomerChurnNoticeSwitch();
                    if (WeConstans.DEL_FOLLOW_USER_SWITCH_OPEN.equals(customerChurnNoticeSwitch)){
                        WeCustomer weCustomer = weCustomerService.selectWeCustomerById(message.getExternalUserId());
                        String content = "您已经被客户@"+weCustomer.getName()+"删除!" ;
                        TextMessageDto textMessageDto = new TextMessageDto();
                        textMessageDto.setContent(content);
                        WeMessagePushDto weMessagePushDto = new WeMessagePushDto();
                        weMessagePushDto.setMsgtype(MessageType.TEXT.getMessageType());
                        weMessagePushDto.setTouser(message.getUserId());
                        weMessagePushDto.setText(textMessageDto);
                        Optional.ofNullable(validWeCorpAccount).map(WeCorpAccount::getAgentId).ifPresent(agentId -> {
                            weMessagePushDto.setAgentid(Integer.valueOf(agentId));
                        });
                        weMessagePushClient.sendMessageToUser(weMessagePushDto);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("del_follow_user>>>>>>>>>>>>>param:{},ex:{}", JSONObject.toJSONString(message),e);
        }
    }
}
