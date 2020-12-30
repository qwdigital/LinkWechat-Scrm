package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.dto.message.QueryCustomerMessageStatusResultDataObjectDto;
import com.linkwechat.wecom.domain.dto.message.QueryCustomerMessageStatusResultDto;
import com.linkwechat.wecom.domain.dto.message.SendMessageResultDto;
import com.linkwechat.wecom.domain.dto.message.WeCustomerMessagePushDto;

/**
 * @description: 群发消息
 * @author: KeWen
 * @create: 2020-10-25 21:34
 **/
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
     * <a href="https://work.weixin.qq.com/api/doc/90000/90135/92136">API文档地址</a>
     *
     * @param queryCustomerMessageStatusResultDataObjectDto{msgid} <a href="https://work.weixin.qq.com/api/doc/90000/90135/92135">添加企业群发消息任务返回的msgid</a>
     */
    @Request(url = "/externalcontact/get_group_msg_result",
            type = "POST"
    )
    QueryCustomerMessageStatusResultDto queryCustomerMessageStatus(@Body QueryCustomerMessageStatusResultDataObjectDto queryCustomerMessageStatusResultDataObjectDto);


}
