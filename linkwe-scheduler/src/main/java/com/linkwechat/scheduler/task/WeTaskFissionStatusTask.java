package com.linkwechat.scheduler.task;

import com.linkwechat.common.enums.LockEnums;
import com.linkwechat.service.IWeFissionService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 任务状态处理
 * @date 2021/4/12 14:39
 **/
@Slf4j
@Component
public class WeTaskFissionStatusTask {


    @Autowired
    private IWeFissionService iWeFissionService;

    @Autowired
    private RedissonClient redissonClient;

    @XxlJob("weTaskFissionStatusTask")
    public void taskFissionExpiredStatusHandle() {
        RLock lock = redissonClient.getLock(LockEnums.WE_FASSION_LOCK.getCode());
        if(lock.tryLock()){
            try {
                log.info("裂变相关处理--------------------------start");
                iWeFissionService.handleFission();
                //处理过期任务
                iWeFissionService.handleExpireFission();
                log.info("裂变相关处理--------------------------end");
            }finally {
               lock.unlock();
           }
        }

    }
}
