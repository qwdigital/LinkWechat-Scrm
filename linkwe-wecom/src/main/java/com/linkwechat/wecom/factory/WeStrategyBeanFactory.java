package com.linkwechat.wecom.factory;

import com.linkwechat.wecom.domain.callback.WeBackBaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author danmo
 * @description 策略工厂
 * @date 2021/1/20 22:02
 **/
@Service
public class WeStrategyBeanFactory {
    @Autowired
    private final Map<String, WeEventStrategy> eventStrategyMap = new ConcurrentHashMap<>();

    public WeStrategyBeanFactory(Map<String, WeEventStrategy> strategyMap) {
        this.eventStrategyMap.putAll(strategyMap);
    }

    public void getResource(String type, WeBackBaseVo message) {
        eventStrategyMap.get(type).eventHandle(message);
    }
}
