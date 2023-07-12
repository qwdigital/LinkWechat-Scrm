package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.leads.leads.entity.WeLeadsAutoRecovery;

/**
 * 线索自动回收 服务类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 14:43
 */
public interface IWeLeadsAutoRecoveryService extends IService<WeLeadsAutoRecovery> {

    /**
     * 添加自动回收
     *
     * @param leadsId    线索Id
     * @param followerId 跟进人Id
     * @param seaId      公海Id
     * @return {@link Long}
     * @author WangYX
     * @date 2023/07/12 10:59
     */
    Long save(Long leadsId, Long followerId, Long seaId);

}
