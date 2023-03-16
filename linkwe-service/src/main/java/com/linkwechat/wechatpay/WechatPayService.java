package com.linkwechat.wechatpay;


import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.exception.wecom.WeComException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 微信支付配置
 *
 * @author danmo
 * @date 2023年03月13日 17:21
 */


@Slf4j
@Component
public class WechatPayService {

    //@Autowired
    //@Qualifier("WxPayClient")
    private CloseableHttpClient wxPayClient;


    /**
     * 发送POST请求
     *
     * @param url    请求地址
     * @param params json参数
     * @return obj
     */
    public JSONObject sendPost(String url, JSONObject params) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setEntity(new StringEntity(params.toJSONString(), StandardCharsets.UTF_8));
        CloseableHttpResponse response = wxPayClient.execute(httpPost);
        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                log.info("微信POST请求成功,返回的结果是 = " + bodyAsString);
            } else if (statusCode == 204) {
                log.info("微信POST请求成功成功，未返回结果");
            } else {
                log.info("微信POST请求失败,响应码 = " + statusCode + ",返回结果 = " + bodyAsString);
                JSONObject body = JSONObject.parseObject(bodyAsString);
                throw new RuntimeException(body.getString("message"));
            }
            return JSONObject.parseObject(bodyAsString);
        } catch (Exception e) {
            log.error("微信支付V3请求失败", e);
            throw new RuntimeException(e.getMessage());
        } finally {
            response.close();
        }
    }

    /**
     * 发送get请求
     *
     * @param url 请求地址 参数直接在地址上拼接
     * @return obj
     */
    public String sendGet(String url) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.addHeader("Accept", "application/json");
        CloseableHttpResponse response = wxPayClient.execute(httpGet);
        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                log.info("微信GET请求成功,返回的结果是 = " + bodyAsString);
            } else if (statusCode == 204) {
                log.info("微信GET请求成功成功，未返回结果");
            } else {
                log.info("微信GET请求失败,响应码 = " + statusCode + ",返回结果 = " + bodyAsString);
                JSONObject body = JSONObject.parseObject(bodyAsString);
                throw new RuntimeException(body.getString("message"));
            }
            return bodyAsString;
        } catch (Exception e) {
            log.error("微信支付V3请求失败", e);
            throw new RuntimeException(e.getMessage());
        } finally {
            response.close();
        }
    }

}
