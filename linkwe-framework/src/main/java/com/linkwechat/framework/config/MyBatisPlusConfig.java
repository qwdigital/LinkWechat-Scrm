package com.linkwechat.framework.config;


import com.github.pagehelper.PageInterceptor;
import com.linkwechat.common.config.RuoYiConfig;
import com.linkwechat.common.config.WeComeConfig;

import org.apache.ibatis.plugin.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * Mybatis支持*匹配扫描包
 * 
 * @author ruoyi
 */
@Configuration
public class MyBatisPlusConfig {
    @Autowired
    WeComeConfig weComeConfig;
    @Autowired
    RuoYiConfig ruoYiConfig;


    @Bean
    public Interceptor[] plugins() {

        return new Interceptor[]{new PageInterceptor()};

//        if(ruoYiConfig.isStartTenant()){ //多租户相关
//            MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//            interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(
//                    new TenantLineHandler() {
//                        // 获取租户 ID 值表达式，只支持单个 ID 值
//                        @Override
//                        public Expression getTenantId() {
//
//                            try {
//                                WeCorpAccount weCorpAccount
//                                        = SecurityUtils.getLoginUser().getUser().getWeCorpAccount();
//                                if(null != weCorpAccount){
//                                    return new StringValue(weCorpAccount.getCorpId());
//                                }
//                            }catch (Exception e){
//                                return null;
//                            }
//
//                            return null;
//                        }
//                        // 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件,
//                        // 这里设置 role表不需要该条件
//                        @Override
//                        public boolean ignoreTable(String tableName) {
//
//                            if(this.getTenantId()==null){
//                                   return true;
//                            }
//
//
//                            if (Arrays.asList(weComeConfig.getNeedTenant())
//                                    .contains(tableName)){
//                                return false;
//                            }
//                            return true;
//                        }
//
//                        @Override
//                        public String getTenantIdColumn() {
//
//                            return "corp_id";
//                        }
//                    }));
//            return new Interceptor[]{interceptor,new PageInterceptor()
//            };
//
//        }else{
//            return new Interceptor[]{new PageInterceptor()};
//        }

    }
}