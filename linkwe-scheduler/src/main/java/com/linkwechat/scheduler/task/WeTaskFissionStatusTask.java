package com.linkwechat.scheduler.task;

import com.linkwechat.service.IWeFissionService;
import com.xxl.job.core.context.XxlJobHelper;
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
    private IWeFissionService iWeFissionService;

    @XxlJob("weTaskFissionStatusTask")
    public void taskFissionExpiredStatusHandle() {
        String params = XxlJobHelper.getJobParam();
        log.info("裂变相关处理--------------------------start");
        iWeFissionService.handleFission();
        log.info("裂变相关处理--------------------------end");
    }
}
