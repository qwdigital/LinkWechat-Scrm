package com.linkwechat.wecom;

import com.dtflys.forest.springboot.annotation.ForestScan;
import com.linkwechat.common.constant.WeServerNameConstants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author danmo
 * @description
 * @date 2022/3/9 14:26
 **/
@MapperScan("com.linkwechat.**.mapper")
@ComponentScan("com.linkwechat.**")
@ForestScan("com.linkwechat.**")
@SpringBootApplication
public class LinkWeChatWecomApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(LinkWeChatWecomApplication.class)
                .properties("spring.config.name:bootstrap", "config/run/bootstrap.yml")
                .properties("spring.application.name="+ WeServerNameConstants.linkweWecom)
                .build().run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  linkwe-wecom启动成功   ლ(´ڡ`ლ)ﾞ ");
    }
}
