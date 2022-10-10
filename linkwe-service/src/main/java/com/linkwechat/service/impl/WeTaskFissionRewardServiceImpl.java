package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.mapper.WeTaskFissionRewardMapper;
import com.linkwechat.domain.WeTaskFissionReward;
import com.linkwechat.service.IWeTaskFissionRewardService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 任务裂变奖励(WeTaskFissionReward)
 *
 * @author danmo
 * @since 2022-06-28 13:48:55
 */
@Service
public class WeTaskFissionRewardServiceImpl extends ServiceImpl<WeTaskFissionRewardMapper, WeTaskFissionReward> implements IWeTaskFissionRewardService {

public WeTaskFissionRewardServiceImpl() {}

@Autowired
private WeTaskFissionRewardMapper weTaskFissionRewardMapper;

    @Override
    public List<WeTaskFissionReward> getRewardList(WeTaskFissionReward reward) {
        LambdaQueryWrapper<WeTaskFissionReward> wrapper = new LambdaQueryWrapper<>();
        if(null != reward.getTaskFissionId()){
            wrapper.eq(WeTaskFissionReward::getTaskFissionId,reward.getTaskFissionId());
        }
        if(StringUtils.isNotEmpty(reward.getRewardCode())){
            wrapper.eq(WeTaskFissionReward::getRewardCode,reward.getRewardCode());
        }
        if(null != reward.getRewardCodeStatus()){
            wrapper.eq(WeTaskFissionReward::getRewardCodeStatus,reward.getRewardCodeStatus());
        }
        if(StringUtils.isNotEmpty(reward.getRewardUserId())){
            wrapper.eq(WeTaskFissionReward::getRewardUserId,reward.getRewardUserId());
        }
        wrapper.eq(WeTaskFissionReward::getDelFlag,0);
        return list(wrapper);
    }
}
