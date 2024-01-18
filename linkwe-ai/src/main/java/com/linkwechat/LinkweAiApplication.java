package com.linkwechat;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import com.linkwechat.common.config.fegin.FeginConfig;
import com.linkwechat.common.constant.WeServerNameConstants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;


@MapperScan("com.linkwechat.**.mapper")
@SpringBootApplication(exclude = {PageHelperAutoConfiguration.class})
@EnableAsync
@EnableFeignClients(defaultConfiguration = FeginConfig.class)
public class LinkweAiApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(LinkweAiApplication.class)
                .properties("spring.config.name:bootstrap", "config/run/bootstrap.yml")
                .properties("spring.application.name="+ WeServerNameConstants.linkweAi)
                .build().run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  LinkWe-ai启动成功   ლ(´ڡ`ლ)ﾞ ");
    }

}
