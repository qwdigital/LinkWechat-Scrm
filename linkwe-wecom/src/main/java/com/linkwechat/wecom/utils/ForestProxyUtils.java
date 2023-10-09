package com.linkwechat.wecom.utils;

import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestProxy;
import com.linkwechat.common.utils.StringUtils;


/**
 * 设置代理
 */
public class ForestProxyUtils {


    //设置http代理
    public static void setProxy(ForestRequest req, String ip,int port,String password,String userName){
        ForestProxy proxy=new ForestProxy(ip,port);

        if(StringUtils.isNotEmpty(password)){
            proxy.setPassword(password);
        }

        if(StringUtils.isNotEmpty(userName)){
            proxy.setUsername(userName);
        }

        req.proxy(proxy);

    }


}
