package com.linkwechat.config.redisson;

import cn.hutool.core.util.StrUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson配置
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/05/15 14:42
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String post;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private Integer database;

    @Bean
    public RedissonClient singleServerConfig() {
        //使用单机模式
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://" + host + ":" + post);
        if (StrUtil.isNotBlank(password)) {
            singleServerConfig.setPassword(password);
        }
        if (database != null) {
            singleServerConfig.setDatabase(database);
        }
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
