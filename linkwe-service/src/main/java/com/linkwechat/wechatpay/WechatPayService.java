package com.linkwechat.wechatpay;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

/**
 * 微信支付接口类
 *
 * @author danmo
 * @date 2023年03月13日 17:21
 */
@Service
@Slf4j
public abstract class WechatPayService {

    private CloseableHttpClient httpClient = null;

    /**
     * 商户号
     */
    public static String mchId = "1613220424";
    /**
     * 商户API私钥路径
     */
    public static String privateKey = "D:\\Windows\\system32\\WXCertUtil\\cert\\apiclient_key.pem";
    /**
     * 商户证书序列号
     */
    public static String mchSerialNo = "799B9A9B7361FD0D54ABCC31DDDD2896313C0055";
    /**
     * 商户APIV3密钥
     */
    public static String apiV3Key = "3b141e1a3c6245038681A504c5a6Qwas";


    private void before() throws Exception {
        // 加载商户私钥（privateKey：私钥字符串）
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new FileInputStream(ResourceUtils.getFile(privateKey)));

        // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
        CertificatesManager verifier = CertificatesManager.getInstance();
        verifier.putMerchant(mchId, new WechatPay2Credentials(mchId,
                new PrivateKeySigner(mchSerialNo, merchantPrivateKey)), apiV3Key.getBytes(StandardCharsets.UTF_8));

        // 初始化httpClient
        httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, mchSerialNo, merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier.getVerifier(mchId))).build();
    }


    private void after() throws Exception {
        httpClient.close();
    }

    public abstract String getRequestUrl(String query);

    /**
     * 请求GET接口
     *
     * @return
     */
    public String sendGetRequest(String query) throws Exception {
        before();
        String result = "";

        String url = getRequestUrl(query);
        //请求URL
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");
        //完成签名并执行请求
        CloseableHttpResponse response = httpClient.execute(httpGet);
        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) { //处理成功
                log.info("success,return body = " + EntityUtils.toString(response.getEntity()));
                result = EntityUtils.toString(response.getEntity());
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("success");
            } else {
                log.error("failed,resp code = " + statusCode + ",return body = " + EntityUtils.toString(response.getEntity()));
            }
        } finally {
            response.close();
        }
        after();
        return result;
    }

}
