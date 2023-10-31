package com.linkwechat.scheduler.service.impl.crowd;

import com.linkwechat.common.enums.strategiccrowd.CorpAddStateEnum;
import com.linkwechat.common.enums.strategiccrowd.CrowdSwipeTypeEnum;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.strategic.crowd.WeStrategicCrowdSwipe;
import com.linkwechat.scheduler.service.AbstractCrowdService;
import com.linkwechat.service.IWeCustomerService;
import com.linkwechat.service.IWeStrategicCrowdStateTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 渠道标签人群计算
 * @author sxw
 */

@Service
@Slf4j
public class CrowdCorpStateTagServiceImpl extends AbstractCrowdService {


    @Override
    public List<WeCustomer> calculate(WeStrategicCrowdSwipe crowdSwipe) {

        CorpAddStateEnum corpAddStateEnum = CorpAddStateEnum.parseEnum(Integer.valueOf(crowdSwipe.getCode()));
        if(corpAddStateEnum != null){
            return SpringUtils.getBean(corpAddStateEnum.getMethod(), IWeStrategicCrowdStateTagService.class)
                    .getStateTagCustomerList(crowdSwipe.getValue());
        }
        return null;


    }
}
