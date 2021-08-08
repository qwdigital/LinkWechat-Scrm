package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Header;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.dto.WeMessagePushDto;
import com.linkwechat.wecom.domain.dto.WeMessagePushGroupDto;
import com.linkwechat.wecom.domain.dto.WeMessagePushResultDto;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;

/**
 * @description: 消息推送
 * @author: KeWen
 * @create: 2020-10-17 22:41
 **/
@SuppressWarnings("all")
@BaseRequest(baseURL = "${weComServerUrl}${weComePrefix}", interceptor = WeAccessTokenInterceptor.class)
public interface WeMessagePushClient {

    /**
     * 发送应用消息
     */
    @Request(url = "/message/send",
            type = "POST"
    )
    WeMessagePushResultDto sendMessageToUser(@JSONBody WeMessagePushDto weMessagePushDto, @Header("agentId") String agentId);

    /**
     * 应用推送消息
     */
    @Request(url = "/appchat/send",
            type = "POST"
    )
    WeMessagePushResultDto sendMessageToUserGroup(@JSONBody WeMessagePushGroupDto weMessagePushGroupDto);

}
