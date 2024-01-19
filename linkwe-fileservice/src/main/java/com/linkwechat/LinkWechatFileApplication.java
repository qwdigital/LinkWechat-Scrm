package com.linkwechat;

import com.linkwechat.common.constant.WeServerNameConstants;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;


/**
 * 文件服务
 *
 * @author leejoker
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
public class LinkWechatFileApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(LinkWechatFileApplication.class)
                .properties("spring.config.name:bootstrap", "config/run/bootstrap.yml")
                .properties("spring.application.name="+ WeServerNameConstants.linkweFile)
                .build().run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  LinkWe-file启动成功   ლ(´ڡ`ლ)ﾞ ");
    }



}
