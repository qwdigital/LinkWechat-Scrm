package com.linkwechat.wecom.factory.impl;

import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.Threads;
import com.linkwechat.wecom.domain.dto.WeEmpleCodeDto;
import com.linkwechat.wecom.domain.dto.WeWelcomeMsg;
import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeCallBackEventFactory;
import com.linkwechat.wecom.service.IWeCustomerService;
import com.linkwechat.wecom.service.IWeEmpleCodeService;
import com.linkwechat.wecom.service.IWeFlowerCustomerRelService;
import com.linkwechat.wecom.service.IWeMsgTlpScopeService;
import lombok.extern.slf4j.Slf4j;

import me.chanjar.weixin.cp.bean.external.msg.Image;
import me.chanjar.weixin.cp.bean.external.msg.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sxw
 * @description 外部联系人回调事件处理
 * @date 2020/11/9 14:52
 **/
@Service
@Slf4j
public class WeEventChangeExternalContactImpl implements WeCallBackEventFactory {
    @Autowired
    private IWeEmpleCodeService weEmpleCodeService;
    @Autowired
    private IWeMsgTlpScopeService weMsgTlpScopeService;
    @Autowired
    private IWeCustomerService weCustomerService;
    @Autowired
    private IWeFlowerCustomerRelService weFlowerCustomerRelService;

    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        String changeType = message.getChangeType();
        switch (changeType) {
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
        if (StringUtils.isNotEmpty(chatId)) {
            //客户群变更事件
            weChatChangeEvent(message);
        }
    }

    private void weChatChangeEvent(WxCpXmlMessageVO message) {
    }

    private void transferFail(WxCpXmlMessageVO message) {
    }

    private void delFollowUser(WxCpXmlMessageVO message) {
        if (message.getUserId() != null && message.getExternalUserId() != null) {
            weFlowerCustomerRelService.deleteFollowUser(message.getUserId(), message.getExternalUserId());
        }
    }

    private void delExternalContact(WxCpXmlMessageVO message) {
        if (message.getExternalUserId() != null) {
            weCustomerService.deleteCustomersByEid(message.getExternalUserId());
        }
    }

    private void addHalfExternalContact(WxCpXmlMessageVO message) {
        if (message.getExternalUserId() != null) {
            weCustomerService.getCustomersInfoAndSynchWeCustomer(message.getExternalUserId());
        }
    }

    private void editExternalContact(WxCpXmlMessageVO message) {
        if (message.getExternalUserId() != null) {
            weCustomerService.getCustomersInfoAndSynchWeCustomer(message.getExternalUserId());
        }
    }

    private void addExternalContact(WxCpXmlMessageVO message) {
        if (message.getExternalUserId() != null) {
            weCustomerService.getCustomersInfoAndSynchWeCustomer(message.getExternalUserId());
        }
        try {
            Threads.SINGLE_THREAD_POOL.submit(new Runnable() {
                @Override
                public void run() {
                    //向扫码客户发送欢迎语
                    if (message.getState() != null && message.getWelcomeCode() != null) {
                        log.info("执行发送欢迎语>>>>>>>>>>>>>>>");
                        WeWelcomeMsg.WeWelcomeMsgBuilder weWelcomeMsgBuilder = WeWelcomeMsg.builder().welcome_code(message.getWelcomeCode());
                        WeEmpleCodeDto messageMap = weEmpleCodeService.selectWelcomeMsgByActivityScene(message.getState(),message.getUserId());
                        if (messageMap != null) {
                            if (StringUtils.isNotEmpty(messageMap.getWelcomeMsg())){
                                Text text = new Text();
                                text.setContent(messageMap.getWelcomeMsg());
                                weWelcomeMsgBuilder.text(text);
                            }
                            if(StringUtils.isNotEmpty(messageMap.getCategoryId())){
                                Image image = new Image();
                                image.setMediaId(messageMap.getCategoryId());
                                image.setPicUrl(messageMap.getMaterialUrl());
                                weWelcomeMsgBuilder.image(image);
                            }
                            weCustomerService.sendWelcomeMsg(weWelcomeMsgBuilder.build());
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error("执行发送欢迎语失败！",e);
        }
    }
}
