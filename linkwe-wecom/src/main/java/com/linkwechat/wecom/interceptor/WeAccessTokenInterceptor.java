package com.linkwechat.wecom.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import com.dtflys.forest.utils.ForestDataType;
import com.linkwechat.common.config.WeComeConfig;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.service.IWeAccessTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * @description: 微信token拦截器
 * @author: HaoN
 * @create: 2020-08-27 22:36
 **/
@Slf4j
@Component
public class WeAccessTokenInterceptor implements Interceptor{


    @Autowired
    private  IWeAccessTokenService iWeAccessTokenService;


    @Autowired
    private WeComeConfig weComeConfig;

    @Resource
    private ForestConfiguration forestConfiguration;



    /**
     * 该方法在请求发送之前被调用, 若返回false则不会继续发送请求
     */
    @Override
    public boolean beforeExecute(ForestRequest request) {
        String weComServerUrl = String.valueOf(forestConfiguration.getVariableValue("weComServerUrl"));
        String weComePrefix = String.valueOf(forestConfiguration.getVariableValue("weComePrefix"));
        String uri=request.getUrl().replace(weComServerUrl+weComePrefix,"");

        if(!Arrays.asList(weComeConfig.getFileUplodUrl()).contains(uri)){
            request.setDataType(ForestDataType.JSON);
            request.setContentType("application/json");
        }
        // 添加请求参数access_token
        if(!Arrays.asList(weComeConfig.getNoAccessTokenUrl()).contains(uri)){
            String token="";

            if(Arrays.asList(weComeConfig.getNeedContactTokenUrl()).contains(uri)){ //需要联系人token
                token=iWeAccessTokenService.findContactAccessToken();
            }else if(Arrays.asList(weComeConfig.getNeedProviderTokenUrl()).contains(uri)){ //需要供应商token
                token=iWeAccessTokenService.findProviderAccessToken();
            }else if(Arrays.asList(weComeConfig.getNeedChatTokenUrl()).contains(uri)){ //需要会话存档token
                token=iWeAccessTokenService.findChatAccessToken();
            }else  if(Arrays.asList(weComeConfig.getThirdAppUrl()).contains(uri)){ //第三方自建应用token
                token=iWeAccessTokenService.findThirdAppAccessToken(
                        StrUtil.isEmpty(request.getHeaderValue(WeConstans.THIRD_APP_PARAM_TIP))?(String) request.getQuery(WeConstans.THIRD_APP_PARAM_TIP):request.getHeaderValue(WeConstans.THIRD_APP_PARAM_TIP)
                );
            } else{
                token=iWeAccessTokenService.findCommonAccessToken();
            }
            request.addQuery("access_token",token);
        }
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
     *  请求成功调用(微信端错误异常统一处理)
     * @param o
     * @param forestRequest
     * @param forestResponse
     */
    @Override
    public void onSuccess(Object o, ForestRequest forestRequest, ForestResponse forestResponse) {
        log.info("url:{},result:{}",forestRequest.getUrl(),forestResponse.getContent());
        WeResultDto weResultDto = JSONUtil.toBean(forestResponse.getContent(), WeResultDto.class);
        if(null != weResultDto.getErrcode() && !weResultDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)&& !weResultDto.getErrcode().equals(WeConstans.NOT_EXIST_CONTACT) ){
            throw new ForestRuntimeException(forestResponse.getContent());
        }
    }


}
