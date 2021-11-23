package com.linkwechat.wecom.interceptor;

import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.interceptor.Interceptor;
import com.linkwechat.common.config.WeComeConfig;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.service.IWeAccessTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;


/**
 * 响应文件流
 */
@Slf4j
@Component
public class WeAccessTokenFileInterceptor implements Interceptor<InputStream> {

    @Autowired
    private IWeAccessTokenService iWeAccessTokenService;



    /**
     * 该方法在请求发送之前被调用, 若返回false则不会继续发送请求
     */
    @Override
    public boolean beforeExecute(ForestRequest request) {
        String token = iWeAccessTokenService.findContactAccessToken();
        request.replaceOrAddQuery("access_token", token);
        return true;
    }

}
