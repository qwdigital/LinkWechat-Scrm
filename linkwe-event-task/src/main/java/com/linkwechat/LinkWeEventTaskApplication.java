package com.linkwechat;

import com.linkwechat.common.config.fegin.FeginConfig;
import com.linkwechat.common.constant.WeServerNameConstants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@MapperScan("com.linkwechat.**.mapper")
@SpringBootApplication
@EnableFeignClients(basePackages="com.linkwechat.**",defaultConfiguration = FeginConfig.class)
@EnableAsync
public class LinkWeEventTaskApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(LinkWeEventTaskApplication.class)
                .properties("spring.config.name:bootstrap", "config/run/bootstrap.yml")
                .properties("spring.application.name="+ WeServerNameConstants.linkweEventTask)
                .build().run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  LinkWe-event-task启动成功   ლ(´ڡ`ლ)ﾞ ");
    }

}
