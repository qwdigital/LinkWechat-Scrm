package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.agent.query.WeAgentQuery;
import com.linkwechat.domain.wecom.vo.agent.vo.WeAgentDetailVo;
import com.linkwechat.domain.wecom.vo.agent.vo.WeAgentListVo;
import com.linkwechat.fallback.QwAgentFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author danmo
 * @date 2022/11/03 23:00
 */
@FeignClient(value = "${wecom.serve.linkwe-wecom}", fallback = QwAgentFallbackFactory.class, contextId = "linkwe-wecom-agent")
public interface QwAgentClient {

    /**
     * 应用列表
     *
     * @param query 入参
     * @return WeAgentListVo
     */
    @PostMapping("/agent/getList")
    public AjaxResult<WeAgentListVo> getList(@RequestBody WeAgentQuery query);

    /**
     * 应用详情
     *
     * @param query 入参
     * @return WeAgentDetailVo
     */
    @PostMapping("/agent/getAgentDetail")
    public AjaxResult<WeAgentDetailVo> getAgentDetail(@RequestBody WeAgentQuery query);

    /**
     * 设置应用
     *
     * @param query 入参
     * @return WeResultVo
     */
    @PostMapping("/agent/updateAgent")
    public AjaxResult<WeResultVo> updateAgent(@RequestBody WeAgentQuery query);

}
