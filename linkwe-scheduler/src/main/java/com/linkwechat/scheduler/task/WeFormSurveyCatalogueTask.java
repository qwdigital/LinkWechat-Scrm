package com.linkwechat.scheduler.task;

import com.linkwechat.domain.WeFormSurveyCatalogue;
import com.linkwechat.service.IWeFormSurveyCatalogueService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 智能表单定时收集任务处理
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/15 11:34
 */
@Slf4j
@Component
public class WeFormSurveyCatalogueTask {

    @Autowired
    private IWeFormSurveyCatalogueService weFormSurveyCatalogueService;

    @XxlJob("weFormSurveyCatalogueTask")
    public void process(String params) {
        log.info("智能表单定时收集任务>>>>>>>>>>>>>>>>>>启动 params:{}", params);
        List<WeFormSurveyCatalogue> list = weFormSurveyCatalogueService.getListIgnoreTenantId();
        if (list != null && list.size() > 0) {
            for (WeFormSurveyCatalogue weFormSurveyCatalogue : list) {
                //判断是否是定时收集的表单
                if (weFormSurveyCatalogue.getAnTiming() != null && weFormSurveyCatalogue.getAnTiming().equals(1)) {
                    //判断表单的状态
                    Integer surveyState = weFormSurveyCatalogue.getSurveyState();
                    Date timingStart = weFormSurveyCatalogue.getTimingStart();
                    Date timingEnd = weFormSurveyCatalogue.getTimingEnd();
                    long currentTimeMillis = System.currentTimeMillis();
                    boolean flag = false;
                    switch (surveyState) {
                        case 0:
                            //表单状态为未发布,判断是否需要更新为收集中状态
                            if (timingStart.getTime() <= currentTimeMillis) {
                                weFormSurveyCatalogue.setSurveyState(1);
                                flag = true;
                            }
                            break;
                        case 1:
                            //表单状态为收集中，判断是否需要更新为已结束
                        case 2:
                            //表单状态为已暂停，判断是否需要更新为已结束
                            if (timingEnd.getTime() <= currentTimeMillis) {
                                weFormSurveyCatalogue.setSurveyState(3);
                                flag = true;
                            }
                            break;
                    }
                    if (flag) {
                        weFormSurveyCatalogueService.updateByIdIgnoreTenantId(weFormSurveyCatalogue);
                    }
                }
            }
        }
    }
}
