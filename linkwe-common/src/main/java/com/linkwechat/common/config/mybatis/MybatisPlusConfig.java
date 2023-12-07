package com.linkwechat.common.config.mybatis;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.github.pagehelper.PageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author leejoker
 * @version 1.0
 * @date 2022/2/23 13:20
 */
@Configuration
public class MybatisPlusConfig {
    /**
     * 一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存万一出现问题
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new DataScopeInterceptor(new PearlDataScopeHandler()));
        return interceptor;
    }

    @Bean
    public BatchSqlInjector easySqlInjector () {
        return new BatchSqlInjector();
    }

        //pagehelper 分页配置
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return new ConfigurationCustomizer() {
            @Override
            public void customize(MybatisConfiguration configuration) {
                PageInterceptor pageInterceptor = new PageInterceptor();
                Properties properties = new Properties();
                properties.setProperty("helperDialect", "mysql");
                pageInterceptor.setProperties(properties);
                configuration.addInterceptor(pageInterceptor);
            }
        };
    }

}
