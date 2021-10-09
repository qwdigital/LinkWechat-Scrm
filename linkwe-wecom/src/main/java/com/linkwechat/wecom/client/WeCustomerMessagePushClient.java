package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.wecom.domain.dto.message.QueryCustomerMessageStatusResultDataObjectDto;
import com.linkwechat.wecom.domain.dto.message.QueryCustomerMessageStatusResultDto;
import com.linkwechat.wecom.domain.dto.message.SendMessageResultDto;
import com.linkwechat.wecom.domain.dto.message.WeCustomerMessagePushDto;
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
    @Request(url = "/externalcontact/add_msg_template",
            type = "POST"
    )
    SendMessageResultDto sendCustomerMessageToUser(@Body WeCustomerMessagePushDto customerMessagePushDto);

    /**
     * 获取企业群发消息发送结果
     * <a href="https://work.weixin.qq.com/api/doc/90000/90135/93338#%E8%8E%B7%E5%8F%96%E4%BC%81%E4%B8%9A%E7%BE%A4%E5%8F%91%E6%88%90%E5%91%98%E6%89%A7%E8%A1%8C%E7%BB%93%E6%9E%9C">API文档地址</a>
     *
     * @param queryCustomerMessageStatusResultDataObjectDto{msgid} <a href="https://work.weixin.qq.com/api/doc/90000/90135/92135">添加企业群发消息任务返回的msgid</a>
     */
    @Request(url = "/externalcontact/get_group_msg_result",
            type = "POST"
    )
    QueryCustomerMessageStatusResultDto queryCustomerMessageStatus(@Body QueryCustomerMessageStatusResultDataObjectDto queryCustomerMessageStatusResultDataObjectDto);





}
