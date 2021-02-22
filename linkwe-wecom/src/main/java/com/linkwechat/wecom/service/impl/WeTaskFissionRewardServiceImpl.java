package com.linkwechat.wecom.service.impl;

import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.wecom.domain.WeTaskFissionReward;
import com.linkwechat.wecom.mapper.WeTaskFissionRewardMapper;
import com.linkwechat.wecom.service.IWeTaskFissionRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务裂变奖励Service业务层处理
 *
 * @author leejoker
 * @date 2021-01-20
 */
@Service
public class WeTaskFissionRewardServiceImpl implements IWeTaskFissionRewardService {
    @Autowired
    private WeTaskFissionRewardMapper weTaskFissionRewardMapper;

    /**
     * 查询任务裂变奖励
     *
     * @param id 任务裂变奖励ID
     * @return 任务裂变奖励
     */
    @Override
    public WeTaskFissionReward selectWeTaskFissionRewardById(Long id) {
        return weTaskFissionRewardMapper.selectWeTaskFissionRewardById(id);
    }

    /**
     * 查询任务裂变奖励列表
     *
     * @param weTaskFissionReward 任务裂变奖励
     * @return 任务裂变奖励
     */
    @Override
    public List<WeTaskFissionReward> selectWeTaskFissionRewardList(WeTaskFissionReward weTaskFissionReward) {
        return weTaskFissionRewardMapper.selectWeTaskFissionRewardList(weTaskFissionReward);
    }

    /**
     * 新增任务裂变奖励
     *
     * @param weTaskFissionReward 任务裂变奖励
     * @return 结果
     */
    @Override
    public int insertWeTaskFissionReward(WeTaskFissionReward weTaskFissionReward) {
        weTaskFissionReward.setCreateTime(DateUtils.getNowDate());
        return weTaskFissionRewardMapper.insertWeTaskFissionReward(weTaskFissionReward);
    }

    /**
     * 修改任务裂变奖励
     *
     * @param weTaskFissionReward 任务裂变奖励
     * @return 结果
     */
    @Override
    public int updateWeTaskFissionReward(WeTaskFissionReward weTaskFissionReward) {
        return weTaskFissionRewardMapper.updateWeTaskFissionReward(weTaskFissionReward);
    }

    /**
     * 批量删除任务裂变奖励
     *
     * @param ids 需要删除的任务裂变奖励ID
     * @return 结果
     */
    @Override
    public int deleteWeTaskFissionRewardByIds(Long[] ids) {
        return weTaskFissionRewardMapper.deleteWeTaskFissionRewardByIds(ids);
    }

    /**
     * 删除任务裂变奖励信息
     *
     * @param id 任务裂变奖励ID
     * @return 结果
     */
    @Override
    public int deleteWeTaskFissionRewardById(Long id) {
        return weTaskFissionRewardMapper.deleteWeTaskFissionRewardById(id);
    }
}
