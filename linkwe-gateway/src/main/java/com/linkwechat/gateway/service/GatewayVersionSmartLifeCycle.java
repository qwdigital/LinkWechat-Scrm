package com.linkwechat.gateway.service;

import com.linkwechat.common.constant.CacheConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.retry.Repeat;
import javax.annotation.Resource;
import java.time.Duration;

@Slf4j
@Component
public class GatewayVersionSmartLifeCycle implements SmartLifecycle {

    private boolean isRunning = false;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private GatewayDynamicRouteService publisher;

    private Disposable disposable;

    @Override
    public void start() {
        /* 每10秒检查当前route version是否最新 不是最新时刷新
         * 监听 RefreshRoutesResultEvent 得到刷新结果
         * 刷新失败的原因大概率是因为路由规则设置错误 失败时重置版本为 0 不断重试
         */
        disposable =
                Mono.defer(() -> {
                            if(!stringRedisTemplate.hasKey(CacheConstants.GATEWAY_ROUTES)){
                                //重新初始化
                                this.publisher.init();
                            }
                            return Mono.empty();
                        })
                        .repeatWhen(
                                Repeat.onlyIf(repeatContext -> true)
                                        .fixedBackoff(Duration.ofSeconds(1)))
                        .subscribeOn(Schedulers.boundedElastic()).subscribe();
        isRunning = true;
    }

    @Override
    public void stop() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

}
