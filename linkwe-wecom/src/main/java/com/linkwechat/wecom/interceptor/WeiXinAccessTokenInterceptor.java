package com.linkwechat.wecom.interceptor;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import com.dtflys.forest.utils.ForestDataType;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.weixin.dto.WxBaseResultDto;
import com.linkwechat.wecom.domain.weixin.dto.WxTokenDto;
import com.linkwechat.wecom.wxclient.WxCommonClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author danmo
 * @description 微信请求拦截器
 * @date 2021/4/5 15:17
 **/
@Slf4j
@Component
public class WeiXinAccessTokenInterceptor implements Interceptor {
    //暂时写到配置文件，之后写入数据库
    @Value("${weixin.appid}")
    private String appId;
    @Value("${weixin.secret}")
    private String secret;
    private final String grantType = "client_credential";

    @Autowired
    private WxCommonClient wxCommonClient;

    @Autowired
    private RedisCache redisCache;

    @Override
    public boolean beforeExecute(ForestRequest request){
        request.setDataType(ForestDataType.JSON);
        String accessToken = findAccessToken();
        request.addQuery("access_token",accessToken);
        return true;
    }


    private String findAccessToken(){
        //获取用户token
        String accessToken =redisCache.getCacheObject(WeConstans.WX_ACCESS_TOKEN);
        if (StringUtils.isEmpty(accessToken)){
            //当用户token失效，重新获取token
            WxTokenDto wxTokenDto = wxCommonClient.getToken(grantType, appId, secret);
            if(wxTokenDto != null && StringUtils.isNotEmpty(wxTokenDto.getAccessToken())){
                redisCache.setCacheObject(WeConstans.WX_AUTH_ACCESS_TOKEN, wxTokenDto.getAccessToken(), wxTokenDto.getExpiresIn(), TimeUnit.SECONDS);
                accessToken = wxTokenDto.getAccessToken();
            }
        }
        return accessToken;
    }


    /**
     *  请求发送失败时被调用
     * @param e
     * @param request
     * @param response
     */
    @Override
    public void onError(ForestRuntimeException e, ForestRequest request, ForestResponse response) {
        log.info("url:{},------params:{},----------result:"+request.getUrl(),
                JSONObject.toJSONString(request.getArguments()),
                response.getContent());
        throw new ForestRuntimeException(response.getContent());
    }

    @Override
    public void onSuccess(Object data, ForestRequest request, ForestResponse response) {
        log.info("url:【{}】,result:【{}】",request.getUrl(),response.getContent());
        WxBaseResultDto wxBaseResultDto = JSONUtil.toBean(response.getContent(), WxBaseResultDto.class);
        if(null != wxBaseResultDto.getErrcode() && !wxBaseResultDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
            throw new ForestRuntimeException(response.getContent());
        }
    }
}
