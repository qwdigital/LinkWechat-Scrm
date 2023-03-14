package com.linkwechat.wechatpay;

import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信支付实现类
 * @author danmo
 * @date 2023年03月13日 17:21
 */
@Slf4j
@Service
public class WechatPayServiceImpl extends WechatPayService{

    @Override
    public String getRequestUrl(String query) {
        return null;
    }
}
