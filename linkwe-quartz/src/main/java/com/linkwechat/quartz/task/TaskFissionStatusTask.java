package com.linkwechat.quartz.task;

import com.linkwechat.wecom.service.IWeTaskFissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 任务状态处理
 * @date 2021/4/12 14:39
 **/
@Slf4j
@Component("TaskFissionStatusTask")
public class TaskFissionStatusTask {
    @Autowired
    private IWeTaskFissionService weTaskFissionService;


    public void taskFissionExpiredStatusHandle() {
        log.info("任务宝过期时间处理--------------------------start");
        weTaskFissionService.updateExpiredWeTaskFission();
        log.info("任务宝过期时间处理--------------------------end");
    }
}
