package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.wecom.domain.dto.message.*;
import com.linkwechat.wecom.domain.query.WeAddMsgTemplateQuery;
import com.linkwechat.wecom.domain.query.WeGetGroupMsgListQuery;
import com.linkwechat.wecom.domain.query.WeGroupMsgListQuery;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * @description: 群发消息
 * @author: KeWen
 * @create: 2020-10-25 21:34
 **/
@BaseRequest(baseURL = "${weComServerUrl}${weComePrefix}", interceptor = WeAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeCustomerMessagePushClient {


    /**
     * 添加企业群发消息任务
     * <a href="https://work.weixin.qq.com/api/doc/90000/90135/92135">API文档地址</a>
     */
    @Deprecated
    @Request(url = "/externalcontact/add_msg_template", type = "POST")
    SendMessageResultDto sendCustomerMessageToUser(@JSONBody WeCustomerMessagePushDto customerMessagePushDto);



    /**
     * 获取企业群发消息发送结果
     * <a href="https://work.weixin.qq.com/api/doc/90000/90135/93338#%E8%8E%B7%E5%8F%96%E4%BC%81%E4%B8%9A%E7%BE%A4%E5%8F%91%E6%88%90%E5%91%98%E6%89%A7%E8%A1%8C%E7%BB%93%E6%9E%9C">API文档地址</a>
     *
     * @param queryCustomerMessageStatusResultDataObjectDto{msgid} <a href="https://work.weixin.qq.com/api/doc/90000/90135/92135">添加企业群发消息任务返回的msgid</a>
     */
    @Deprecated
    @Request(url = "/externalcontact/get_group_msg_result", type = "POST")
    QueryCustomerMessageStatusResultDto queryCustomerMessageStatus(@JSONBody QueryCustomerMessageStatusResultDataObjectDto queryCustomerMessageStatusResultDataObjectDto);



    /**
     * 创建企业群发
     *
     * @param query {@link WeAddMsgTemplateQuery}
     */
    @Request(url = "/externalcontact/add_msg_template", type = "POST")
    SendMessageResultDto addMsgTemplate(@JSONBody WeAddMsgTemplateQuery query);

    /**
     * 获取群发记录列表
     *
     * @param query {@link WeGroupMsgListQuery}
     * @return {@link WeGroupMsgListDto}
     */
    @Request(url = "/externalcontact/get_groupmsg_list_v2",type = "POST")
    WeGroupMsgListDto getGroupMsgList(@JSONBody WeGroupMsgListQuery query);

    /**
     * 获取群发成员发送任务列表
     *
     * @param query {@link WeGetGroupMsgListQuery}
     * @return {@link WeGroupMsgListDto}
     */
    @Request(url = "/externalcontact/get_groupmsg_task",type = "POST")
    WeGroupMsgListDto getGroupMsgTask(@JSONBody WeGetGroupMsgListQuery query);


    /**
     * 获取企业群发成员执行结果
     *
     * @param query {@link WeGetGroupMsgListQuery}
     * @return {@link WeGroupMsgListDto}
     */
    @Request(url = "/externalcontact/get_groupmsg_send_result",type = "POST")
    WeGroupMsgListDto getGroupMsgSendResult(@JSONBody WeGetGroupMsgListQuery query);
}
