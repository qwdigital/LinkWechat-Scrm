package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.msgaudit.WeMsgAuditQuery;
import com.linkwechat.domain.wecom.vo.msgaudit.WeMsgAuditVo;
import com.linkwechat.fallback.QwMsgAuditFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author danmo
 * @date 2023年05月12日 10:17
 */
@FeignClient(value = "${wecom.serve.linkwe-wecom}", fallback = QwMsgAuditFallbackFactory.class)
public interface QwMsgAuditClient {

    /**
     * 获取会话内容存档开启成员列表
     */
    @PostMapping("/msg/audit/getPermitUserList")
    public AjaxResult<WeMsgAuditVo> getPermitUserList(@RequestBody WeMsgAuditQuery query);

    /**
     * 获取会话中外部成员的同意情况
     */
    @PostMapping("/msg/audit/checkSingleAgree")
    public AjaxResult<WeMsgAuditVo> checkSingleAgree(@RequestBody WeMsgAuditQuery query);

    /**
     * 查询对应roomid里面所有外企业的外部联系人的同意情况
     */
    @PostMapping("/msg/audit/checkRoomAgree")
    public AjaxResult<WeMsgAuditVo> checkRoomAgree(@RequestBody WeMsgAuditQuery query);

    /**
     * 获取会话内容存档内部群信息
     */
    @PostMapping("/msg/audit/getGroupChat")
    public AjaxResult<WeMsgAuditVo> getGroupChat(@RequestBody WeMsgAuditQuery query);
}
