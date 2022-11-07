package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeAgentInfo;
import com.linkwechat.domain.agent.query.WeAgentAddQuery;
import com.linkwechat.domain.agent.query.WeAgentEditQuery;
import com.linkwechat.domain.agent.vo.LwAgentListVo;

import java.util.List;

/**
 * 应用信息表(WeAgentInfo)
 *
 * @author danmo
 * @since 2022-11-04 17:08:08
 */
public interface IWeAgentInfoService extends IService<WeAgentInfo> {

    Integer addAgent(WeAgentAddQuery query);
    /**
     * 拉取应用信息
     * @param id
     */
    void pullAgent(Integer id);

    /**
     * 编辑应用信息
     * @param query
     */
    void update(WeAgentEditQuery query);

    /**
     * 删除应用
     * @param id
     */
    void deleteAgent(Integer id);

    /**
     * 获取应用列表
     * @return
     */
    List<LwAgentListVo> getList();


    WeAgentInfo getAgentInfoByAgentId(Integer agentId);


}
