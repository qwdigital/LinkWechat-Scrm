package com.linkwechat.framework.config;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.github.pagehelper.PageInterceptor;
import com.linkwechat.common.config.WeComeConfig;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;


/**
 * Mybatis支持*匹配扫描包
 * 
 * @author ruoyi
 */
@Configuration
public class MyBatisPlusConfig
{
    @Autowired
    WeComeConfig weComeConfig;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 如果用了分页插件注意先 add TenantLineInnerInterceptor 再 add PaginationInnerInterceptor
        // 用了分页插件必须设置 MybatisConfiguration#useDeprecatedExecutor = false
        //        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());

        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(
                new TenantLineHandler() {
                    // manager_id = 1088248166370832385

                    // 获取租户 ID 值表达式，只支持单个 ID 值
                    @Override
                    public Expression getTenantId() {
                        return new LongValue(1088248166370832385L);
                    }
                    // 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件,
                    // 这里设置 role表不需要该条件
                    @Override
                    public boolean ignoreTable(String tableName) {

                        if (Arrays.asList(weComeConfig.getNeedTenant())
                                .contains(tableName)){
                            return false;
                        }
                        return true;
                    }

                    @Override
                    public String getTenantIdColumn() {
                        return "corp_id";
                    }
                }));
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }

    @Bean
    public Interceptor[] plugins() {
        return new Interceptor[]{new PageInterceptor()};
    }
}