package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.leads.leads.entity.WeLeadsFollower;
import com.linkwechat.domain.leads.leads.vo.WeLeadsFollowerNumVO;

/**
 * 线索跟进人 服务类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 14:43
 */
public interface IWeLeadsFollowerService extends IService<WeLeadsFollower> {

    /**
     * 获取跟进人的跟进线索数量数据
     *
     * @param seaId 公海Id
     * @return {@link WeLeadsFollowerNumVO}
     * @author WangYX
     * @date 2023/07/11 17:05
     */
    WeLeadsFollowerNumVO getLeadsFollowerNum(Long seaId);
}
