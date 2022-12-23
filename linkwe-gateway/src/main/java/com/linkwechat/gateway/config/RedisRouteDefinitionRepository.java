package com.linkwechat.gateway.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.CacheConstants;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author leejoker
 * @version 1.0
 * @date 2021/11/24 10:30
 */
@Component
public class RedisRouteDefinitionRepository implements RouteDefinitionRepository {


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinition> routeDefinitions = Lists.newArrayList();
        stringRedisTemplate.opsForHash().values(CacheConstants.GATEWAY_ROUTES).forEach(routeDefinition -> routeDefinitions.add(
                JSON.parseObject(routeDefinition.toString(), RouteDefinition.class)));


        return Flux.fromIterable(routeDefinitions);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(routeDefinition -> {
            stringRedisTemplate.opsForHash()
                    .put(CacheConstants.GATEWAY_ROUTES, routeDefinition.getId(), JSONObject.toJSONString(routeDefinition));
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            if (stringRedisTemplate.opsForHash().hasKey(CacheConstants.GATEWAY_ROUTES, id)) {
                stringRedisTemplate.opsForHash().delete(CacheConstants.GATEWAY_ROUTES, id);
                return Mono.empty();
            }
            return Mono.defer(
                    () -> Mono.error(new NotFoundException("route definition is not found, routeId:" + routeId)));
        });
    }
}
