package com.linkwechat.scheduler.task;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.enums.LockEnums;
import com.linkwechat.domain.WeFormSurveyCatalogue;
import com.linkwechat.service.IWeFormSurveyCatalogueService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

    @Autowired
    private RedissonClient redissonClient;


    /**
     * 表单状态维护 表单状态;0默认设计状态未发布，1收集中，2已暂停, 3已结束
     */
    @XxlJob("weFormSurveyCatalogueTask")
    public void process() {

        RLock lock = redissonClient.getLock(LockEnums.WE_FORM_SURVEY_STATE_LOCK.getCode());

        if(lock.tryLock()){
            try {

                log.info("智能表单定时收集任务>>>>>>>>>>>>>>>>>>启动 params:{}");
                List<WeFormSurveyCatalogue> weFormSurveyCatalogues = weFormSurveyCatalogueService.list(new LambdaQueryWrapper<WeFormSurveyCatalogue>()
                        .isNotNull(WeFormSurveyCatalogue::getTimingStart)
                        .ne(WeFormSurveyCatalogue::getSurveyState,3));
                if(CollectionUtil.isNotEmpty(weFormSurveyCatalogues)){

                    weFormSurveyCatalogues.stream().forEach(k->{
                        //当前时间在结束时间和开始时间之间,表单设置为 1 收集中(暂停状态下改场景不做处理)
                        if(!new Integer(2).equals(k.getSurveyState())){
                            if(System.currentTimeMillis()>k.getTimingStart().getTime()&&k.getTimingEnd()==null){
                                k.setSurveyState(1);
                            }else if(System.currentTimeMillis()>k.getTimingStart().getTime()&&System.currentTimeMillis()<k.getTimingEnd().getTime()){
                                k.setSurveyState(1);
                            }
                        }

                        //当前时间小于结束时间，表单状态设置为 3 已结束
                        if(k.getTimingEnd() != null&&System.currentTimeMillis()>k.getTimingEnd().getTime()){
                            k.setSurveyState(3);
                        }
                    });


                    weFormSurveyCatalogueService.updateBatchById(weFormSurveyCatalogues);

                }

            }finally {
                lock.unlock();
            }


        }


    }
}
