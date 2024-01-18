package com.linkwechat.scheduler;

import com.linkwechat.common.config.fegin.FeginConfig;
import com.linkwechat.common.constant.WeServerNameConstants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@ComponentScan("com.linkwechat.**")
@MapperScan("com.linkwechat.**.mapper")
@EnableFeignClients(basePackages="com.linkwechat.**",defaultConfiguration = FeginConfig.class)
@SpringBootApplication
public class LinkWechatSchedulerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(LinkWechatSchedulerApplication.class)
                .properties("spring.config.name:bootstrap", "config/run/bootstrap.yml")
                .properties("spring.application.name="+ WeServerNameConstants.linkweScheduler)
                .build().run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  LinkWe-scheduler启动成功   ლ(´ڡ`ლ)ﾞ ");
    }
}
