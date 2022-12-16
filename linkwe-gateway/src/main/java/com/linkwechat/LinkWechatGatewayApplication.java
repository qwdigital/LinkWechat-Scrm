package com.linkwechat;


import com.linkwechat.common.config.fegin.FeginConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 网关启动程序
 *
 * @author leejoker
 */
@MapperScan(basePackages = "com.linkwechat.**.mapper")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class})
@EnableFeignClients(basePackages="com.linkwechat.**",defaultConfiguration = FeginConfig.class)
public class LinkWechatGatewayApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(LinkWechatGatewayApplication.class)
                .properties("spring.config.name:bootstrap", "config/run/bootstrap.yml")
                .properties("spring.application.name=linkwe-gateway")
                .build().run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  LinkWe-gateway启动成功   ლ(´ڡ`ლ)ﾞ ");
    }
}
