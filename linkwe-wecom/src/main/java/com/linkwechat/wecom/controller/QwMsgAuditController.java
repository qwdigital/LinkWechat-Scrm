package com.linkwechat.wecom.controller;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.msgaudit.WeMsgAuditQuery;
import com.linkwechat.domain.wecom.vo.msgaudit.WeMsgAuditVo;
import com.linkwechat.wecom.service.IQwMsgAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 会话存档管理
 * @author danmo
 * @date 2023年05月12日 9:56
 */
@RestController
@RequestMapping("msg/audit")
public class QwMsgAuditController {

    @Autowired
    private IQwMsgAuditService qwMsgAuditService;

    /**
     * 获取会话内容存档开启成员列表
     */
    @PostMapping("/getPermitUserList")
    public AjaxResult<WeMsgAuditVo> getPermitUserList(@RequestBody WeMsgAuditQuery query){
        return AjaxResult.success(qwMsgAuditService.getPermitUserList(query));
    }

    /**
     * 获取会话中外部成员的同意情况
     */
    @PostMapping("/checkSingleAgree")
    public AjaxResult<WeMsgAuditVo> checkSingleAgree(@RequestBody WeMsgAuditQuery query){
        return AjaxResult.success(qwMsgAuditService.checkSingleAgree(query));
    }

    /**
     * 查询对应roomid里面所有外企业的外部联系人的同意情况
     */
    @PostMapping("/checkRoomAgree")
    public AjaxResult<WeMsgAuditVo> checkRoomAgree(@RequestBody WeMsgAuditQuery query){
        return AjaxResult.success(qwMsgAuditService.checkRoomAgree(query));
    }

    /**
     * 获取会话内容存档内部群信息
     */
    @PostMapping("/getGroupChat")
    public AjaxResult<WeMsgAuditVo> getGroupChat(@RequestBody WeMsgAuditQuery query){
        return AjaxResult.success(qwMsgAuditService.getGroupChat(query));
    }
}
