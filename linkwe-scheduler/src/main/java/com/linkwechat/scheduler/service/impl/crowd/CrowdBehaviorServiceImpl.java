package com.linkwechat.scheduler.service.impl.crowd;

import cn.hutool.core.date.DateUtil;
import com.linkwechat.common.enums.strategiccrowd.CustomerBehaviorEnum;
import com.linkwechat.common.enums.strategiccrowd.CustomerBehaviorTimeEnum;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.strategic.crowd.WeStrategicCrowdSwipe;
import com.linkwechat.scheduler.service.AbstractCrowdService;
import com.linkwechat.service.IWeStrategicCrowdBehaviorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * 客户行为人群计算
 *
 * @author danmo
 */

@Service
@Slf4j
public class CrowdBehaviorServiceImpl extends AbstractCrowdService {

    @Override
    public List<WeCustomer> calculate(WeStrategicCrowdSwipe crowdSwipe) {
        log.info("客户行为人群计算");
        String startTime = "";
        String endTime = "";
        CustomerBehaviorTimeEnum behaviorTimeEnum = CustomerBehaviorTimeEnum.parseEnum(Integer.valueOf(crowdSwipe.getCode()));

        switch (Objects.requireNonNull(behaviorTimeEnum)){
            case TODAY:
                startTime = DateUtil.beginOfDay(new Date()).toString();
                endTime  = DateUtil.endOfDay(new Date()).toString();
                break;
            case YESTERDAY:
                startTime = DateUtil.beginOfDay(DateUtil.yesterday()).toString();
                endTime  = DateUtil.endOfDay(DateUtil.yesterday()).toString();
                break;
            case LAST_SEVEN_DAYS:
                startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(),-7)).toString();
                endTime  = DateUtil.endOfDay(DateUtil.yesterday()).toString();
                break;
            case LAST_THREE_DAYS:
                startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(),-3)).toString();
                endTime  = DateUtil.endOfDay(DateUtil.yesterday()).toString();
                break;
            case CUSTOMIZE:
                startTime = DateUtil.beginOfDay(DateUtil.parseDate(crowdSwipe.getStartTime())).toString();
                endTime  = DateUtil.endOfDay(DateUtil.parseDate(crowdSwipe.getStartTime())).toString();
                break;
            case INTERVAL:
                startTime = DateUtil.beginOfDay(DateUtil.parseDate(crowdSwipe.getStartTime())).toString();
                endTime  = DateUtil.endOfDay(DateUtil.parseDate(crowdSwipe.getEndTime())).toString();
                break;
            default:
                break;
        }

        CustomerBehaviorEnum customerBehaviorEnum = CustomerBehaviorEnum.parseEnum(Integer.valueOf(crowdSwipe.getBehavior()));
        if(customerBehaviorEnum != null){
            return SpringUtils.getBean(customerBehaviorEnum.getMethod(), IWeStrategicCrowdBehaviorService.class)
                    .getCustomerList(startTime,endTime,Integer.valueOf(crowdSwipe.getHappenType()));
        }
        return null;
    }

}
