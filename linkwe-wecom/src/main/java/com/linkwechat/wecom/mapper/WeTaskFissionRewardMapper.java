package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeTaskFissionReward;

import java.util.List;

/**
 * 任务裂变奖励Mapper接口
 *
 * @author leejoker
 * @date 2021-01-20
 */
public interface WeTaskFissionRewardMapper extends BaseMapper<WeTaskFissionReward> {
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
     * 删除任务裂变奖励
     *
     * @param id 任务裂变奖励ID
     * @return 结果
     */
    public int deleteWeTaskFissionRewardById(Long id);

    /**
     * 批量删除任务裂变奖励
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeTaskFissionRewardByIds(Long[] ids);
}
