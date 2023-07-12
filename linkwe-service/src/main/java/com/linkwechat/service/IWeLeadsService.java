package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.leads.leads.entity.WeLeads;

/**
 * 线索 服务类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 14:43
 */
public interface IWeLeadsService extends IService<WeLeads> {

    /**
     * 领取线索
     *
     * @param leadsId 线索Id
     * @author WangYX
     * @date 2023/07/11 16:15
     */
    void receiveLeads(Long leadsId);

}
