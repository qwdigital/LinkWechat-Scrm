package com.linkwechat.factory;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author danmo
 * @description 客服消息策略工厂
 * @date 2021/1/20 22:02
 **/
@Service
public class WeKfStrategyBeanFactory {
    @Autowired
    private final Map<String, WeKfEventStrategy> eventStrategyMap = new ConcurrentHashMap<>();

    public WeKfStrategyBeanFactory(Map<String, WeKfEventStrategy> strategyMap) {
        this.eventStrategyMap.putAll(strategyMap);
    }

    public void getResource(String type, JSONObject message) {
        eventStrategyMap.get(type).eventHandle(message);
    }
}
