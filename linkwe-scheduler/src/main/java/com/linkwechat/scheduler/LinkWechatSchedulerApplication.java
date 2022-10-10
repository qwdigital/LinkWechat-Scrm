package com.linkwechat.scheduler;

import com.linkwechat.config.fegin.FeginConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
        SpringApplication.run(LinkWechatSchedulerApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  LinkWechat任务调度服务启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
