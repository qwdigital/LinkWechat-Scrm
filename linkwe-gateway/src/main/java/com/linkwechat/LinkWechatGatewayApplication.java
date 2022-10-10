package com.linkwechat;

import com.linkwechat.config.fegin.FeginConfig;
import com.linkwechat.mapper.WeSynchRecordMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
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
        SpringApplication.run(LinkWechatGatewayApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  LinkWechat网关启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
