package com.linkwechat.scheduler.service.impl.crowd;

import cn.hutool.core.collection.CollectionUtil;
import com.linkwechat.common.enums.strategiccrowd.RelationEnum;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.customer.vo.WeCustomersVo;
import com.linkwechat.domain.strategic.crowd.WeStrategicCrowdSwipe;
import com.linkwechat.scheduler.service.AbstractCrowdService;
import com.linkwechat.service.IWeCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 标签组人群计算
 * @author danmo
 */

@Service
@Slf4j
public class CrowdTagGroupServiceImpl extends AbstractCrowdService {

    @Autowired
    public IWeCustomerService weCustomerService;

    @Override
    public List<WeCustomer> calculate(WeStrategicCrowdSwipe crowdSwipe) {


        WeCustomersQuery query = new WeCustomersQuery();
        if(StringUtils.isNotEmpty(crowdSwipe.getRelation()) && RelationEnum.NOT_EQUAL.getCode().equals(crowdSwipe.getRelation())){
            query.setExcludeTagIds(crowdSwipe.getValue());
        }else{
            query.setTagIds(crowdSwipe.getValue());
        }

        List<WeCustomersVo> weCustomerList = weCustomerService.findWeCustomerList(query, null);
        if(CollectionUtil.isNotEmpty(weCustomerList)){
            return weCustomerList.parallelStream().map(item ->{
                WeCustomer weCustomer = new WeCustomer();
                BeanUtils.copyBeanProp(weCustomer,item);
                weCustomer.setAddUserId(item.getFirstUserId());
                weCustomer.setAddTime(item.getFirstAddTime());
//                weCustomer.setId(item.getId());
                return weCustomer;
            }).collect(Collectors.toList());
        }
        return null;
    }
}
