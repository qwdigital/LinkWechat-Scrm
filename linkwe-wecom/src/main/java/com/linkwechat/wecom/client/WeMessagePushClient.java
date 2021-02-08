package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.Header;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.dto.WeMessagePushDto;
import com.linkwechat.wecom.domain.dto.WeMessagePushGroupDto;
import com.linkwechat.wecom.domain.dto.WeMessagePushResultDto;

/**
 * @description: 消息推送
 * @author: KeWen
 * @create: 2020-10-17 22:41
 **/
@SuppressWarnings("all")
public interface WeMessagePushClient {

    /**
     * 发送应用消息
     */
    @Request(url = "/message/send",
            type = "POST"
    )
    WeMessagePushResultDto sendMessageToUser(@DataObject WeMessagePushDto weMessagePushDto,@Header("agentId") String agentId);

    /**
     * 应用推送消息
     */
    @Request(url = "/appchat/send",
            type = "POST"
    )
    WeMessagePushResultDto sendMessageToUserGroup(@DataObject WeMessagePushGroupDto weMessagePushGroupDto);

}
