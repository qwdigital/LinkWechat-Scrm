package com.linkwechat.scheduler.task;

import com.linkwechat.service.IWeTasksService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 待办任务
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/21 16:32
 */
@Slf4j
@Component
public class WeTasksTask {

    @Resource
    private IWeTasksService weTasksService;




    /**
     * 线索长时间未跟进
     * <p>
     * 凌晨执行  0 0 0 * * ?
     *
     * @author WangYX
     * @date 2023/07/21 16:33
     */
    @XxlJob("leadsLongTimeNotFollowUp")
    public void execute() {
        log.info("线索中心-线索长时间未跟进============>");
        weTasksService.handlerLeadsLongTimeNotFollowUp();
    }
}
