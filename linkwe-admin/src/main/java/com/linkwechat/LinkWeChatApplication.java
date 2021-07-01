package com.linkwechat;

import com.dtflys.forest.springboot.annotation.ForestScan;
import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, PageHelperAutoConfiguration.class})
@MapperScan("com.linkwechat.*.mapper")
@ForestScan(basePackages = {"com.linkwechat.wecom.client", "com.linkwechat.wecom.wxclient"})
@EnableAsync
public class LinkWeChatApplication {
    public static void main(String[] args) {

        SpringApplication.run(LinkWeChatApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  LinkWeChat启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }
}
