package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.wecom.domain.dto.WeMessagePushDto;
import com.linkwechat.wecom.domain.dto.WeMessagePushGroupDto;
import com.linkwechat.wecom.domain.dto.WeMessagePushResultDto;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.interceptor.WeAppAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * @description: 消息推送
 * @author: KeWen
 * @create: 2020-10-17 22:41
 **/
@SuppressWarnings("all")
@BaseRequest(baseURL = "${weComServerUrl}${weComePrefix}",retryer = WeCommonRetryWhen.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeMessagePushClient {

    /**
     * 发送应用消息
     */
    @Request(url = "/message/send", type = "POST", interceptor = WeAppAccessTokenInterceptor.class)
    WeMessagePushResultDto sendMessageToUser(@JSONBody WeMessagePushDto weMessagePushDto, @Header("agentId") String agentId);

    /**
     * 应用推送消息
     */
    @Request(url = "/appchat/send", type = "POST",interceptor = WeAccessTokenInterceptor.class)
    WeMessagePushResultDto sendMessageToUserGroup(@JSONBody WeMessagePushGroupDto weMessagePushGroupDto);

}
