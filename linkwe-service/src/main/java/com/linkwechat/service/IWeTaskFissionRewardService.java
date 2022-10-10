package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeTaskFissionReward;

import java.util.List;

/**
 * 任务裂变奖励(WeTaskFissionReward)
 *
 * @author danmo
 * @since 2022-06-28 13:48:55
 */
public interface IWeTaskFissionRewardService extends IService<WeTaskFissionReward> {

    List<WeTaskFissionReward> getRewardList(WeTaskFissionReward reward);
}
