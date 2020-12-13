package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.dto.WeMessagePushResultDto;
import com.linkwechat.wecom.domain.dto.msgaudit.MsgauditDto;

/**
 * @author sxw
 * @description 会话存档接口
 * @date 2020/12/2 16:45
 **/
public interface WeMsgauditClient {

    /**
     * 获取会话内容存档开启成员列表
     */
    @Request(url = "/msgaudit/get_permit_user_list",
            type = "POST"
    )
    WeMessagePushResultDto sendMessageToUser(@DataObject MsgauditDto msgauditDto);
}
