package com.linkwechat.scheduler.task;

import com.linkwechat.service.IWeTaskFissionService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
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
    private IWeTaskFissionService weTaskFissionService;

    @XxlJob("weTaskFissionStatusTask")
    public void taskFissionExpiredStatusHandle() {
        log.info("任务宝过期时间处理--------------------------start");
        weTaskFissionService.updateExpiredWeTaskFission();
        log.info("任务宝过期时间处理--------------------------end");
    }
}
