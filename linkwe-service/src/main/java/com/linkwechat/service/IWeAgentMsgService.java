package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeAgentMsg;
import com.linkwechat.domain.agent.query.WeAgentMsgAddQuery;
import com.linkwechat.domain.agent.query.WeAgentMsgListQuery;
import com.linkwechat.domain.agent.vo.WeAgentMsgListVo;
import com.linkwechat.domain.agent.vo.WeAgentMsgVo;

import java.util.List;

/**
 * 应用消息表(WeAgentMsg)
 *
 * @author danmo
 * @since 2022-11-04 17:08:08
 */
public interface IWeAgentMsgService extends IService<WeAgentMsg> {
    /**
     * 新增
     * @param query
     */
    void addMsg(WeAgentMsgAddQuery query);

    /**
     * 修改
     * @param query
     */
    void updateMsg(WeAgentMsgAddQuery query);

    /**
     * 删除
     * @param id
     */
    void deleteMsg(Long id);

    /**
     * 详情
     * @param id
     */
    WeAgentMsgVo getMsgInfo(Long id);

    /**
     * 撤回
     * @param id
     */
    void revokeMsgInfo(Long id);

    /**
     * 查询正在等待发送的任务
     * @return
     */
    List<WeAgentMsg> getWaitingList();

    /**
     * 历史消息列表
     * @param query
     * @return
     */
    List<WeAgentMsgListVo> getMsgList(WeAgentMsgListQuery query);
}
