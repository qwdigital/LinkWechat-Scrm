package com.linkwechat.service;

import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.strategic.crowd.query.WeCorpStateTagSourceQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 策略人群来源标签
 *
 * @author danmo
 * @since 2022-07-05 15:33:28
 */
public abstract class IWeStrategicCrowdStateTagService{

    @Autowired
    public IWeCustomerService weCustomerService;

    public abstract List<Map<String, Object>> getStateTagSourceList(WeCorpStateTagSourceQuery query);

    public abstract List<WeCustomer> getStateTagCustomerList(String  code);
}
