package com.linkwechat.service;

import com.linkwechat.domain.WeCustomer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 策略人群行为
 *
 * @author danmo
 * @since 2022-07-05 15:33:28
 */
public abstract class IWeStrategicCrowdBehaviorService {

    @Autowired
    protected IWeCustomerService weCustomerService;

    @Autowired
    protected IWeCustomerTrajectoryService weCustomerTrajectoryService;


    public abstract List<WeCustomer> getCustomerList(String startTime, String endTime, Integer happenType);
}
