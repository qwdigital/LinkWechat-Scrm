package com.linkwechat.wecom.service;

import com.linkwechat.wecom.domain.WeTaskFissionReward;
import com.linkwechat.wecom.domain.vo.WeTaskFissionRewardVo;

import java.util.List;

/**
 * 任务裂变奖励Service接口
 *
 * @author leejoker
 * @date 2021-01-20
 */
public interface IWeTaskFissionRewardService {
    /**
     * 查询任务裂变奖励
     *
     * @param id 任务裂变奖励ID
     * @return 任务裂变奖励
     */
    public WeTaskFissionReward selectWeTaskFissionRewardById(Long id);

    /**
     * 查询任务裂变奖励列表
     *
     * @param weTaskFissionReward 任务裂变奖励
     * @return 任务裂变奖励集合
     */
    public List<WeTaskFissionReward> selectWeTaskFissionRewardList(WeTaskFissionReward weTaskFissionReward);

    /**
     * 新增任务裂变奖励
     *
     * @param weTaskFissionReward 任务裂变奖励
     * @return 结果
     */
    public int insertWeTaskFissionReward(WeTaskFissionReward weTaskFissionReward);

    /**
     * 修改任务裂变奖励
     *
     * @param weTaskFissionReward 任务裂变奖励
     * @return 结果
     */
    public int updateWeTaskFissionReward(WeTaskFissionReward weTaskFissionReward);

    /**
     * 批量删除任务裂变奖励
     *
     * @param ids 需要删除的任务裂变奖励ID
     * @return 结果
     */
    public int deleteWeTaskFissionRewardByIds(Long[] ids);

    /**
     * 删除任务裂变奖励信息
     *
     * @param id 任务裂变奖励ID
     * @return 结果
     */
    public int deleteWeTaskFissionRewardById(Long id);

    /**
     * 根据微信用户id和任务id获取任务裂变奖励详细信息
     * @param fissionId 任务id
     * @param eid  客户id
     * @return
     */
    public WeTaskFissionRewardVo getRewardByFissionId(String fissionId, String eid);
}
