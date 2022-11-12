package com.linkwechat.wecom.service.impl;

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
import com.linkwechat.wecom.client.WeAgentClient;
import com.linkwechat.wecom.client.WeIDTransformClient;
import com.linkwechat.wecom.service.IQwAgentService;
import com.linkwechat.wecom.service.IQwCorpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 应用接口
 * @author: danmo
 * @create: 2022-11-03 14:43
 **/
@Slf4j
@Service
public class QwAgentServiceImpl implements IQwAgentService {

    @Resource
    private WeAgentClient weAgentClient;


    @Override
    public WeAgentListVo getList(WeAgentQuery query) {
        return weAgentClient.getList(query);
    }

    @Override
    public WeAgentDetailVo getAgentDetail(WeAgentQuery query) {
        return weAgentClient.getAgentDetail(query);
    }

    @Override
    public WeResultVo updateAgent(WeAgentQuery query) {
        return weAgentClient.updateAgent(query);
    }
}
