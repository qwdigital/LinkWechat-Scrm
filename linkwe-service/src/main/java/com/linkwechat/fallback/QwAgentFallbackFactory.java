package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.agent.query.WeAgentQuery;
import com.linkwechat.domain.wecom.vo.agent.vo.WeAgentDetailVo;
import com.linkwechat.domain.wecom.vo.agent.vo.WeAgentListVo;
import com.linkwechat.fegin.QwAgentClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @date 2022/11/03 23:00
 */
@Component
@Slf4j
public class QwAgentFallbackFactory implements QwAgentClient {


    @Override
    public AjaxResult<WeAgentListVo> getList(WeAgentQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeAgentDetailVo> getAgentDetail(WeAgentQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> updateAgent(WeAgentQuery query) {
        return null;
    }
}
