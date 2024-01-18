package com.linkwechat.gateway.controller;

import cn.hutool.core.map.MapUtil;
import com.linkwechat.common.constant.WeServerNameConstants;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.gateway.service.GatewayDynamicRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author leejoker
 * @version 1.0
 * @date 2021/11/24 14:57
 */
@RestController
@RequestMapping("/gateway")
public class GatewayDynamicRouteController {


    @Autowired
    private DiscoveryClient discoveryClient;


    /**
     * 获取服务状态
     * @return
     */
    @GetMapping("/findServerState")
    public AjaxResult findServerState(){
        List<ServiceInstance> instances = discoveryClient.getInstances(WeServerNameConstants.linkweAi);
        return AjaxResult.success( MapUtil.builder(WeServerNameConstants.linkweAi,instances.size()>0?true:false));
    }

}
