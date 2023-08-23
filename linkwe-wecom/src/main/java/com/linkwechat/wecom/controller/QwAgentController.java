package com.linkwechat.wecom.controller;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.agent.query.WeAgentQuery;
import com.linkwechat.domain.wecom.vo.agent.vo.WeAgentDetailVo;
import com.linkwechat.domain.wecom.vo.agent.vo.WeAgentListVo;
import com.linkwechat.service.IWeCustomerLinkService;
import com.linkwechat.wecom.service.IQwAgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author danmo
 * @description 企微应用接口
 * @date 2022/11/03 21:01
 **/
@Slf4j
@RestController
@RequestMapping("agent")
public class QwAgentController {

    @Autowired
    private IQwAgentService qwAgentService;

    /**
     * 应用列表
     *
     * @param query 入参
     * @return WeAgentListVo
     */
    @PostMapping("/getList")
    public AjaxResult<WeAgentListVo> getList(@RequestBody WeAgentQuery query) {
        WeAgentListVo weAgentList = qwAgentService.getList(query);
        return AjaxResult.success(weAgentList);
    }

    /**
     * 应用详情
     *
     * @param query 入参
     * @return WeAgentDetailVo
     */
    @PostMapping("/getAgentDetail")
    public AjaxResult<WeAgentDetailVo> getAgentDetail(@RequestBody WeAgentQuery query) {
        WeAgentDetailVo weAgentDetail = qwAgentService.getAgentDetail(query);
        return AjaxResult.success(weAgentDetail);
    }

    /**
     * 设置应用
     *
     * @param query 入参
     * @return WeResultVo
     */
    @PostMapping("/updateAgent")
    public AjaxResult<WeResultVo> updateAgent(@RequestBody WeAgentQuery query) {
        WeResultVo weResult = qwAgentService.updateAgent(query);
        return AjaxResult.success(weResult);
    }
}
