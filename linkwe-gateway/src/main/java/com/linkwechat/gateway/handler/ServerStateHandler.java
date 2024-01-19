package com.linkwechat.gateway.handler;

import cn.hutool.core.map.MapUtil;
import com.linkwechat.common.constant.WeServerNameConstants;
import com.linkwechat.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class ServerStateHandler implements HandlerFunction<ServerResponse> {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {

        List<ServiceInstance> instances = discoveryClient.getInstances(WeServerNameConstants.linkweAi);
        Map<String,Boolean> map=new HashMap<>();
        map.put(WeServerNameConstants.linkweAi, instances.size() > 0 ? true : false);
        AjaxResult success = AjaxResult.success(map);
        return ServerResponse.status(HttpStatus.OK).body(BodyInserters.fromValue(
                success
        ));
    }
}
