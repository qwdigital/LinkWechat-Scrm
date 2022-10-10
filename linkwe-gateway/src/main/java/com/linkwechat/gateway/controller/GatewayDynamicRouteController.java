package com.linkwechat.gateway.controller;

import com.linkwechat.gateway.service.GatewayDynamicRouteService;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author leejoker
 * @version 1.0
 * @date 2021/11/24 14:57
 */
@RestController
@RequestMapping("/gateway")
public class GatewayDynamicRouteController {
    @Resource
    private GatewayDynamicRouteService gatewayDynamicRouteService;

    @GetMapping("")
    public Flux<RouteDefinition> list() {
        return gatewayDynamicRouteService.list();
    }

    @PostMapping("")
    public String add(@RequestBody RouteDefinition entity) {
        int result = gatewayDynamicRouteService.add(entity);
        return String.valueOf(result);
    }

    @PutMapping("")
    public String update(@RequestBody RouteDefinition entity) {
        int result = gatewayDynamicRouteService.update(entity);
        return String.valueOf(result);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable("id") String id) {
        return gatewayDynamicRouteService.delete(id);
    }
}
