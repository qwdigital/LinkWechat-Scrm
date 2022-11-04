package com.linkwechat.wecom.service;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.agentdev.WeTransformExternalUserIdQuery;
import com.linkwechat.domain.wecom.query.agentdev.WeTransformUserIdQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.agent.query.WeAgentQuery;
import com.linkwechat.domain.wecom.vo.agent.vo.WeAgentDetailVo;
import com.linkwechat.domain.wecom.vo.agent.vo.WeAgentListVo;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformCorpVO;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformExternalUserIdVO;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformUserIdVO;

/**
 * @author danmo
 * @Description 应用业务接口
 * @date 2022/11/03 17:12
 **/
public interface IQwAgentService {
    /**
     * 获取应用列表
     * @param query
     * @return
     */
    WeAgentListVo getList(WeAgentQuery query);

    /**
     * 获取应用详情
     * @param query
     * @return
     */
    WeAgentDetailVo getAgentDetail(WeAgentQuery query);

    /**
     * 设置应用
     * @param query
     * @return
     */
    WeResultVo updateAgent(WeAgentQuery query);
}
