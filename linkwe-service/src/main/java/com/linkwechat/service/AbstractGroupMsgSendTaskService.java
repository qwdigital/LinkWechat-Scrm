package com.linkwechat.service;

import cn.hutool.core.util.ObjectUtil;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.wecom.query.customer.msg.WeAddCustomerMsgQuery;
import com.linkwechat.domain.wecom.vo.customer.msg.WeAddCustomerMsgVo;
import com.linkwechat.fegin.QwCustomerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author danmo
 * @description 群发消息发送发送业务员
 * @date 2022/4/14 19:03
 **/
@Service
@Slf4j
public abstract class AbstractGroupMsgSendTaskService {

    @Autowired
    IWeMaterialService weMaterialService;

    @Autowired
    LinkWeChatConfig linkWeChatConfig;



    @Autowired
    QwCustomerClient qwCustomerClient;


    /**
     * 具体业务处理消息
     * @param query
     * @return
     */
    public abstract void sendGroupMsg(WeAddGroupMessageQuery query);


    /**
     * 调用企业微信api，构建群发任务
     * @param query
     * @param sender
     * @return
     */
    protected WeAddCustomerMsgVo sendSpecGroupMsgTemplate(WeAddGroupMessageQuery query, WeAddGroupMessageQuery.SenderInfo sender){

        WeAddCustomerMsgQuery templateQuery = new WeAddCustomerMsgQuery();
        templateQuery.setChat_type(query.getChatType());
        templateQuery.setSender(sender.getUserId());
        if (ObjectUtil.equal(1, query.getChatType())) {
            templateQuery.setExternal_userid(sender.getCustomerList());
        }

        weMaterialService.msgTplToMediaId(query.getAttachmentsList());
        templateQuery.setAttachmentsList(linkWeChatConfig.getH5Domain(),query.getAttachmentsList());
        return qwCustomerClient.addMsgTemplate(templateQuery).getData();
    }

}
