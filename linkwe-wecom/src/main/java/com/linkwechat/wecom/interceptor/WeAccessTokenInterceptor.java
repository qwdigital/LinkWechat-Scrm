package com.linkwechat.wecom.interceptor;

import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;

/**
 * @description: 微信token拦截器
 * @author: HaoN
 * @create: 2020-08-27 22:36
 **/
public class WeAccessTokenInterceptor implements Interceptor {


    /**
     * 该方法在请求发送之前被调用, 若返回false则不会继续发送请求
     */
    @Override
    public boolean beforeExecute(ForestRequest request) {


        // 1.从缓存中获取token,如果缓存中不存在,则调用接口,重新获取。

        // 获取到的的token,校验是否失效,如果失效了,则重新获取

        request.addQuery("access_token", "11111111");  // 添加URL的Query参数
        return true;  // 继续执行请求返回true
    }



    /**
     *  请求发送失败时被调用
     * @param e
     * @param forestRequest
     * @param forestResponse
     */
    @Override
    public void onError(ForestRuntimeException e, ForestRequest forestRequest, ForestResponse forestResponse) {

    }

    /**
     * 该方法在请求成功响应时被调用
     * @param o
     * @param forestRequest
     * @param forestResponse
     */
    @Override
    public void onSuccess(Object o, ForestRequest forestRequest, ForestResponse forestResponse) {

    }
}
