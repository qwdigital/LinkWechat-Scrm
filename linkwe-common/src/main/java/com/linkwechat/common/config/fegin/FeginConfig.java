package com.linkwechat.common.config.fegin;

import com.linkwechat.common.interceptor.FeignRequestInterceptor;
import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author danmo
 * @description fegin配置
 * @date 2021/5/31 11:18
 **/
@Configuration
public class FeginConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
    @Bean
    Logger feignLogger() {
        return new FeginLogger(); // 你的继承类
    }

    @Bean
    public Retryer.Default retryerDefault(){
        return new Retryer.Default();
    }


    @Bean
    public RequestInterceptor requestInterceptor()
    {
        return new FeignRequestInterceptor();
    }
}
