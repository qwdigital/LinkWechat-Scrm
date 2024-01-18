package com.linkwechat;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import com.linkwechat.common.config.fegin.FeginConfig;
import com.linkwechat.common.constant.WeServerNameConstants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, PageHelperAutoConfiguration.class})
@MapperScan("com.linkwechat.**.mapper")
@EnableAsync
@EnableFeignClients(defaultConfiguration = FeginConfig.class)
public class LinkWeAuthApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(LinkWeAuthApplication.class)
                .properties("spring.config.name:bootstrap", "config/run/bootstrap.yml")
                .properties("spring.application.name="+ WeServerNameConstants.linkweAuth)
                .build().run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  LinkWe-auth启动成功   ლ(´ڡ`ლ)ﾞ ");
    }
}
