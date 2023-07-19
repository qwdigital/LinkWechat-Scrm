package com.linkwechat.scheduler.task;

import com.linkwechat.service.IWeLeadsAutoRecoveryService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 线索中心-线索自动回收
 * <p>
 * 凌晨执行
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 11:52
 */
@Slf4j
@Component
public class WeLeadsAutoRecoveryTask {

    @Resource
    private IWeLeadsAutoRecoveryService weLeadsAutoRecoveryService;

    @XxlJob("weLeadsAutoRecoveryTask")
    public void execute() {
        log.info("线索中心-线索自动回收启动============>");
        weLeadsAutoRecoveryService.executeAutoRecovery();
    }
}
