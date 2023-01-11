package com.linkwechat.scheduler.task;

import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.service.IWeTaskFissionService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 短链统计
 * @date 2023/1/09 14:39
 **/
@Slf4j
@Component
public class WeShortLinkStatisticTask {
    @Autowired
    private RedisService redisService;

    @XxlJob("weShortLinkStatisticTask")
    public void shortLinkStatisticHandle() {
        log.info("短链统计--------------------------start");
        log.info("短链统计--------------------------end");
    }
}
