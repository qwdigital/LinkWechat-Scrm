package com.linkwechat.service.impl.strategic.state;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.enums.CustomerAddWay;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.strategic.crowd.query.WeCorpStateTagSourceQuery;
import com.linkwechat.service.IWeStrategicCrowdStateTagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QwCorpStateImpl extends IWeStrategicCrowdStateTagService {
    @Override
    public List<Map<String, Object>> getStateTagSourceList(WeCorpStateTagSourceQuery query) {
        return CustomerAddWay.getType();
    }

    @Override
    public List<WeCustomer> getStateTagCustomerList(String code) {
        return weCustomerService.list(new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getAddMethod, code).eq(WeCustomer::getDelFlag,0));
    }
}
