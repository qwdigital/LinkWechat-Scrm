package com.linkwechat.wecom.controller;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.department.WeDeptQuery;
import com.linkwechat.domain.wecom.query.msg.WeAppMsgQuery;
import com.linkwechat.domain.wecom.query.msg.WeRecallMsgQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptVo;
import com.linkwechat.domain.wecom.vo.msg.WeAppMsgVo;
import com.linkwechat.wecom.service.IQwAppMsgService;
import com.linkwechat.wecom.service.IQwDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author leejoker
 * @version 1.0
 * @date 2022/3/23 21:08
 */
@Slf4j
@RestController
@RequestMapping("message")
public class QwAppMsgController {
    @Resource
    private IQwAppMsgService qwAppMsgService;

    /**
     * 应用消息通知
     *
     * @param query
     * @return
     */
    @PostMapping("/send")
    public AjaxResult<WeAppMsgVo> sendAppMsg(@RequestBody WeAppMsgQuery query) {
        WeAppMsgVo weAppMsgVo = qwAppMsgService.sendAppMsg(query);
        return AjaxResult.success(weAppMsgVo);
    }

    @PostMapping("/recall")
    public AjaxResult<WeResultVo> recallAppMsg(@RequestBody WeRecallMsgQuery query) {
        WeResultVo resultVo = qwAppMsgService.recallAppMsg(query);
        return AjaxResult.success(resultVo);
    }


    @PostMapping("/agent/recall")
    public AjaxResult<WeResultVo> recallAgentAppMsg(@RequestBody WeRecallMsgQuery query) {
        WeResultVo resultVo = qwAppMsgService.recallAgentAppMsg(query);
        return AjaxResult.success(resultVo);
    }
}
