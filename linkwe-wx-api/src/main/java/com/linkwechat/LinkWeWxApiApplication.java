package com.linkwechat;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import com.linkwechat.config.fegin.FeginConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@MapperScan("com.linkwechat.**.mapper")
@SpringBootApplication(exclude = {PageHelperAutoConfiguration.class})
@EnableAsync
@EnableFeignClients(defaultConfiguration = FeginConfig.class)
public class LinkWeWxApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(LinkWeWxApiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  LinkWechat微信服务启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }

}
