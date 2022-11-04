package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeAgentInfo;
import com.linkwechat.domain.agent.query.WeAgentAddQuery;
import com.linkwechat.domain.agent.query.WeAgentEditQuery;

/**
 * 应用信息表(WeAgentInfo)
 *
 * @author danmo
 * @since 2022-11-04 17:08:08
 */
public interface IWeAgentInfoService extends IService<WeAgentInfo> {

    /**
     * 拉取应用信息
     * @param query
     */
    void pullAgent(WeAgentAddQuery query);

    /**
     * 编辑应用信息
     * @param query
     */
    void update(WeAgentEditQuery query);

    WeAgentInfo getAgentInfoByAgentId(Integer agentId);

    /**
     * 删除应用
     * @param id
     */
    void deleteAgent(Integer id);
}
