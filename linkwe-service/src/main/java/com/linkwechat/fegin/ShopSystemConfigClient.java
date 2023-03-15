package com.linkwechat.fegin;

import com.linkwechat.fallback.ShopSystemConfigFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * 商城-系统配置
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/12/05 11:07
 */
@FeignClient(value = "${wecom.serve.yshop-mall}", fallback = ShopSystemConfigFallbackFactory.class, contextId = "shopSystemConfigClient")
public interface ShopSystemConfigClient {

    /**
     * 获取商城系统配置
     *
     * @return
     */
    @GetMapping("/mall/get/yxSystemConfig")
    Map<String, Object> getYxSystemConfigs();

    /**
     * 新增或修改
     *
     * @param jsonStr
     * @return
     */
    @PostMapping("/mall/yxSystemConfig")
    Object saveOrUpdate(@RequestBody String jsonStr);

}
