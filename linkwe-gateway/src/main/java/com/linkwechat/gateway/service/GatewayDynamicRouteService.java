package com.linkwechat.gateway.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.linkwechat.common.constant.CacheConstants;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.gateway.config.RedisRouteDefinitionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;


/**
 * @author leejoker
 * @version 1.0
 * @date 2021/11/24 10:38
 */
@Service
@Slf4j
public class GatewayDynamicRouteService implements ApplicationEventPublisherAware{

    @Resource
    private RedisRouteDefinitionRepository redisRouteDefinitionRepository;
    @Resource
    private RedisService redisService;


    private ApplicationEventPublisher applicationEventPublisher;

    private ConfigService configService;

    private static final long timeout = 30000L;

    @Value("${nacos.gateway.route.config.data-id:gateway-router}")
    private String dataId;
    @Value("${nacos.gateway.route.config.group:DEFAULT_GROUP}")
    private String routeGroup;
    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String serverAddress;
    @Value("${spring.cloud.nacos.discovery.namespace:}")
    private String namespace;


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostConstruct
    public void init() {
        log.info("gateway route init...");
        //先清空缓存中旧的路由
        redisService.deleteObject(CacheConstants.GATEWAY_ROUTES);
        try {
            configService = initConfigService();
            if (configService == null) {
                log.warn("initConfigService fail");
                return;
            }
            String configInfo = configService.getConfig(dataId, routeGroup, timeout);
            log.info("获取网关当前配置:\r\n{}", configInfo);
            List<RouteDefinition> definitionList = JSON.parseArray(configInfo, RouteDefinition.class);
            for (RouteDefinition definition : definitionList) {
                log.info("update route : {}", definition.toString());
                add(definition);
            }
        } catch (Exception e) {
            log.error("初始化网关路由时发生错误", e);
        }
        dynamicRouteByNacosListener(dataId, routeGroup);
    }

    /**
     * 初始化网关路由 nacos config
     *
     * @return
     */
    private ConfigService initConfigService() {
        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr", serverAddress);
            if (StringUtils.isNotBlank(namespace)) {
                properties.setProperty("namespace", namespace);
            }
            return configService = NacosFactory.createConfigService(properties);
        } catch (Exception e) {
            log.error("初始化网关路由时发生错误", e);
            return null;
        }
    }

    /**
     * 监听Nacos下发的动态路由配置
     *
     * @param dataId
     * @param group
     */
    public void dynamicRouteByNacosListener(String dataId, String group) {
        try {
            configService.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("进行网关更新:\n\r{}", configInfo);
                    List<RouteDefinition> definitionList = JSON.parseArray(configInfo, RouteDefinition.class);
                    log.info("update route : {}", definitionList.toString());
                    definitionList.forEach(route -> update(route));
                }

                @Override
                public Executor getExecutor() {
                    log.info("getExecutor\n\r");
                    return null;
                }
            });
        } catch (NacosException e) {
            log.error("从nacos接收动态路由配置出错!!!", e);
        }
    }

    public Flux<RouteDefinition> list() {
        return redisRouteDefinitionRepository.getRouteDefinitions();
    }

    public int add(RouteDefinition routeDefinition) {
        redisRouteDefinitionRepository.save(Mono.just(routeDefinition)).subscribe();
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        return 1;
    }

    public int update(RouteDefinition routeDefinition) {
        redisRouteDefinitionRepository.delete(Mono.just(routeDefinition.getId()));
        return add(routeDefinition);
    }

    public Mono<ResponseEntity<Object>> delete(String id) {
        return redisRouteDefinitionRepository.delete(Mono.just(id))
                .then(Mono.defer(() -> Mono.just(ResponseEntity.ok().build())))
                .onErrorResume(t -> t instanceof NotFoundException, t -> Mono.just(ResponseEntity.notFound().build()));
    }



}
