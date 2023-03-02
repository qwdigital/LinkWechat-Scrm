package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeStrategicCrowdCustomerRel;

import java.util.List;
import java.util.Set;

/**
 * 策略人群客户关联表(WeStrategicCrowdCustomerRel)
 *
 * @author danmo
 * @since 2022-07-30 23:56:17
 */
public interface IWeStrategicCrowdCustomerRelService extends IService<WeStrategicCrowdCustomerRel> {

    /**
     * 人群客户Id存储
     * @param crowdId 人群ID
     * @param customerIds 客户ID
     */
    void saveCustomerIds(Long crowdId, Set<Long> customerIds);

    /**
     * 获取列表通过人群ID
     * @param crowdId 人群ID
     * @return
     */
    List<WeStrategicCrowdCustomerRel> getListByCrowdId(Long crowdId);

    /**
     * 更具人群ID和时间查询客户列表
     * @param crowdId 人群ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    List<WeStrategicCrowdCustomerRel> getListByCrowdIdAndTime(Long crowdId, String startTime, String endTime);
}
