package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.WeStrategicCrowdCustomerRel;
import com.linkwechat.mapper.WeStrategicCrowdCustomerRelMapper;
import com.linkwechat.service.IWeStrategicCrowdCustomerRelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 策略人群客户关联表(WeStrategicCrowdCustomerRel)
 *
 * @author danmo
 * @since 2022-07-30 23:56:17
 */
@Service
public class WeStrategicCrowdCustomerRelServiceImpl extends ServiceImpl<WeStrategicCrowdCustomerRelMapper, WeStrategicCrowdCustomerRel> implements IWeStrategicCrowdCustomerRelService {


    @Transactional
    @Override
    public void saveCustomerIds(Long crowdId, Set<Long> customerIds) {
        delCustomerIds(crowdId);
        if(CollectionUtil.isNotEmpty(customerIds)){
            List<WeStrategicCrowdCustomerRel> crowdCustomerRelList = customerIds.parallelStream().map(customerId -> {
                WeStrategicCrowdCustomerRel customerRel = new WeStrategicCrowdCustomerRel();
                customerRel.setCrowdId(crowdId);
                customerRel.setCustomerId(customerId);
                return customerRel;
            }).collect(Collectors.toList());
            saveBatch(crowdCustomerRelList);
        }
    }

    @Override
    public List<WeStrategicCrowdCustomerRel> getListByCrowdId(Long crowdId) {
        return list(new LambdaQueryWrapper<WeStrategicCrowdCustomerRel>()
                .eq(WeStrategicCrowdCustomerRel::getCrowdId,crowdId)
                .eq(WeStrategicCrowdCustomerRel::getDelFlag,0));
    }

    @Override
    public List<WeStrategicCrowdCustomerRel> getListByCrowdIdAndTime(Long crowdId, String startTime, String endTime) {
        return list(new LambdaQueryWrapper<WeStrategicCrowdCustomerRel>()
                .eq(WeStrategicCrowdCustomerRel::getCrowdId,crowdId)
                .le(BaseEntity::getCreateTime,endTime)
                .ge(BaseEntity::getCreateTime,startTime));
    }

    private void delCustomerIds(Long crowdId){
        WeStrategicCrowdCustomerRel customerRel = new WeStrategicCrowdCustomerRel();
        customerRel.setDelFlag(1);
        update(customerRel,new LambdaQueryWrapper<WeStrategicCrowdCustomerRel>()
                .eq(WeStrategicCrowdCustomerRel::getCrowdId,crowdId)
                .eq(WeStrategicCrowdCustomerRel::getDelFlag,0));
    }
}
