package com.linkwechat.wecom.factory.impl;

import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeCallBackEventFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author sxw
 * @description 外部联系人回调事件处理
 * @date 2020/11/9 14:52
 **/
@Service
@Slf4j
public class WeEventChangeExternalContactImpl implements WeCallBackEventFactory {

    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        String changeType = message.getChangeType();
        switch (changeType){
            case "add_external_contact"://添加企业客户事件
                addExternalContact(message);
                break;
            case "edit_external_contact"://编辑企业客户事件
                editExternalContact(message);
                break;
            case "add_half_external_contact"://外部联系人免验证添加成员事件
                addHalfExternalContact(message);
                break;
            case "del_external_contact"://删除企业客户事件
                delExternalContact(message);
                break;
            case "del_follow_user"://删除跟进成员事件
                delFollowUser(message);
                break;
            case "transfer_fail"://客户接替失败事件
                transferFail(message);
                break;
            default:
                break;
        }
        String chatId = message.getChatId();
        if (StringUtils.isNotEmpty(chatId)){
            //客户群变更事件
            weChatChangeEvent(message);
        }
    }

    private void weChatChangeEvent(WxCpXmlMessageVO message) {
    }

    private void transferFail(WxCpXmlMessageVO message) {
    }

    private void delFollowUser(WxCpXmlMessageVO message) {
    }

    private void delExternalContact(WxCpXmlMessageVO message) {
    }

    private void addHalfExternalContact(WxCpXmlMessageVO message) {
    }

    private void editExternalContact(WxCpXmlMessageVO message) {
    }

    private void addExternalContact(WxCpXmlMessageVO message) {

    }
}
