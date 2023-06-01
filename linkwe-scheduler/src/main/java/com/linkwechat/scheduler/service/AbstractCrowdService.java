package com.linkwechat.scheduler.service;

import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.strategic.crowd.WeStrategicCrowdSwipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author danmo
 * @description 人群计算抽象模板类
 * @date 2022/7/14 19:03
 **/
@Service
@Slf4j
public abstract class AbstractCrowdService {
    /**
     * 人群计算
     *
     * @param crowdSwipe
     * @return
     */
    public abstract List<WeCustomer> calculate(WeStrategicCrowdSwipe crowdSwipe);

   /* public void overallPlanning(Long id){
        WeStrategicCrowdDetailVo detail = weStrategicCrowdService.getDetail(id);
        List<WeStrategicCrowdSwipe> swipe = detail.getSwipe();
        for (WeStrategicCrowdSwipe crowdSwipe : swipe) {

            List<WeCustomer> calculate = calculate(crowdSwipe);
        }

    }*/
}
