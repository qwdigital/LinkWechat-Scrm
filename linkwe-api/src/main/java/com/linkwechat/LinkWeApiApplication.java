package com.linkwechat;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import com.linkwechat.common.config.fegin.FeginConfig;
import com.linkwechat.common.constant.WeServerNameConstants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.scheduling.annotation.EnableAsync;

@MapperScan("com.linkwechat.**.mapper")
@SpringBootApplication(exclude = {PageHelperAutoConfiguration.class})
@EnableAsync
@EnableFeignClients(defaultConfiguration = FeginConfig.class)
public class LinkWeApiApplication {
    public static void main(String[] args) {

        new SpringApplicationBuilder(LinkWeApiApplication.class)
                .properties("spring.config.name:bootstrap", "config/run/bootstrap.yml")
                .properties("spring.application.name="+ WeServerNameConstants.linkweApi)
                .build().run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  LinkWe-api启动成功   ლ(´ڡ`ლ)ﾞ ");

    }

}
