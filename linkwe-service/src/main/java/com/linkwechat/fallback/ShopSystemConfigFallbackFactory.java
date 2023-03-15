package com.linkwechat.fallback;

import com.linkwechat.fegin.ShopSystemConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 商城系统配置
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/12/05 11:27
 */
@Slf4j
@Component
public class ShopSystemConfigFallbackFactory implements ShopSystemConfigClient {

    @Override
    public Map<String, Object> getYxSystemConfigs() {
        return null;
    }

    @Override
    public Object saveOrUpdate(String jsonStr) {
        return null;
    }
}
