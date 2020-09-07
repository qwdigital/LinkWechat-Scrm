package com.linkwechat.wecom.interceptor;

import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestHeader;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import com.dtflys.forest.utils.ForestDataType;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.config.WeComeConfig;
import com.linkwechat.wecom.service.IWeAccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 微信token拦截器
 * @author: HaoN
 * @create: 2020-08-27 22:36
 **/
@Component
public class WeAccessTokenInterceptor implements Interceptor{


    @Autowired
    private  IWeAccessTokenService iWeAccessTokenService;


    @Autowired
    private WeComeConfig weComeConfig;



    /**
     * 该方法在请求发送之前被调用, 若返回false则不会继续发送请求
     */
    @Override
    public boolean beforeExecute(ForestRequest request) {
        request.setDataType(ForestDataType.JSON);
        request.setContentType("application/json");
        String uri=request.getUrl().replace("http://","");
        // 添加请求参数access_token
        if(!weComeConfig.getNoAccessTokenUrl().equals(uri)){
            request.addQuery("access_token", iWeAccessTokenService.findToken());
        }

        //添加服务器统一请求地址
        request.setUrl(weComeConfig.getServerUrl()+weComeConfig.getWeComePrefix()+uri);


        return true;
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
     *  请求成功调用
     * @param o
     * @param forestRequest
     * @param forestResponse
     */
    @Override
    public void onSuccess(Object o, ForestRequest forestRequest, ForestResponse forestResponse) {

    }
}
