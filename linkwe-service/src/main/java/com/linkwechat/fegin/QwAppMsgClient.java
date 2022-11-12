package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.msg.WeAppMsgQuery;
import com.linkwechat.domain.wecom.query.msg.WeRecallMsgQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.msg.WeAppMsgVo;
import com.linkwechat.fallback.QwAppMsgFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author leejoker
 * @date 2022/3/29 23:00
 */
@FeignClient(value = "${wecom.serve.linkwe-wecom}", fallback = QwAppMsgFallbackFactory.class, contextId = "linkwe-wecom-message")
public interface QwAppMsgClient {

    /**
     * 应用消息通知
     *
     * @param query
     * @return
     */
    @PostMapping("/message/send")
    public AjaxResult<WeAppMsgVo> sendAppMsg(@RequestBody WeAppMsgQuery query);

    /**
     * 撤回通用应用消息通知
     *
     * @param query
     * @return
     */
    @PostMapping("/message/recall")
    public AjaxResult<WeResultVo> recallAppMsg(@RequestBody WeRecallMsgQuery query);


    /**
     * 撤回应用消息通知
     *
     * @param query
     * @return
     */
    @PostMapping("/message/agent/recall")
    public AjaxResult<WeResultVo> recallAgentAppMsg(@RequestBody WeRecallMsgQuery query);
}
