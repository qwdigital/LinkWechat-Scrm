package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.DataVariable;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.dto.msgaudit.WeMsgAuditDto;
import com.linkwechat.wecom.domain.vo.WeMsgAuditVo;

/**
 * @author danmo
 * @description 会话存档接口
 * @date 2020/12/2 16:45
 **/
public interface WeMsgAuditClient {

    /**
     * 获取会话内容存档开启成员列表
     */
    @Request(url = "/msgaudit/get_permit_user_list",
            type = "POST"
    )
    WeMsgAuditDto getPermitUserList(@DataObject WeMsgAuditDto msgAuditDto);

    /**
     * 单聊 获取会话中外部成员的同意情况
     * @param msgAuditDto
     * @return
     */
    @Request(url = "/msgaudit/check_single_agree",
            type = "POST"
    )
    WeMsgAuditDto checkSingleAgree(@DataObject WeMsgAuditDto msgAuditDto);

    /**
     * 群聊 获取群会话中外部成员的同意情况
     * @param weMsgAuditVo
     * @return
     */
    @Request(url = "/msgaudit/check_room_agree",
            type = "POST"
    )
    WeMsgAuditDto checkRoomAgree(@DataObject WeMsgAuditVo weMsgAuditVo);

    /**
     * 获取会话内容存档内部群信息
     * @param weMsgAuditVo
     * @return
     */
    @Request(url = "/msgaudit/groupchat/get",
            type = "POST"
    )
    WeMsgAuditDto getGroupChat(@DataObject WeMsgAuditVo weMsgAuditVo);
}
