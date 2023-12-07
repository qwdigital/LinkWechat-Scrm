package com.linkwechat.common.config.orika;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Orika配置
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/07 11:40
 */
@Configuration
public class OrikaConfig {

    @Bean
    public MapperFactory mapperFactory() {
        return new DefaultMapperFactory.Builder().build();
    }

    @Bean
    public ConverterFactory converterFactory() {
        ConverterFactory converterFactory = mapperFactory().getConverterFactory();
        converterFactory.registerConverter(new LocalDateConvert());
        return converterFactory;
    }

}
