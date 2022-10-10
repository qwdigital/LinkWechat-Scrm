package com.linkwechat;

import com.linkwechat.config.fegin.FeginConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@MapperScan("com.linkwechat.**.mapper")
@SpringBootApplication
@EnableFeignClients(basePackages="com.linkwechat.**",defaultConfiguration = FeginConfig.class)
@EnableAsync
public class LinkWeEventTaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(LinkWeEventTaskApplication.class, args);
    }

}
