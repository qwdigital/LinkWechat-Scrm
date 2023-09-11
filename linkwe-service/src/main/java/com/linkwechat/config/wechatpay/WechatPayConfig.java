//package com.linkwechat.config.wechatpay;
//
//
//import cn.hutool.core.io.resource.ResourceUtil;
//import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
//import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
//import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
//import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
//import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
//import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
//import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
//import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.security.GeneralSecurityException;
//import java.security.PrivateKey;
//
///**
// * 微信支付配置
// *
// * @author danmo
// * @date 2023年03月13日 17:21
// */
//
//@Data
//@Slf4j
//@Configuration
//@ConfigurationProperties(prefix = "wx-pay")
//public class WechatPayConfig {
//
//    /**
//     * 商户号
//     */
//    private String mchId;
//    /**
//     * 商户API私钥路径
//     */
//    private String privateKeyUrl;
//    /**
//     * 商户证书序列号
//     */
//    public String mchSerialNo;
//    /**
//     * 商户APIV3密钥
//     */
//    public String apiV3Key;
//
//    /**
//     * 获取商户的私钥文件
//     *
//     * @param privateKeyUrl
//     * @return
//     */
//    private PrivateKey getPrivateKey(String privateKeyUrl) {
//        try {
//            String file = ResourceUtil.readUtf8Str(privateKeyUrl);
//            return PemUtil.loadPrivateKey(file);
//        } catch (Exception e) {
//            throw new RuntimeException("私钥文件不存在", e);
//        }
//    }
//
//    /**
//     * 用来获取我的这个微信的Http请求
//     */
//    @Bean(name = "WxPayClient")
//    public CloseableHttpClient getWxPayClient() throws GeneralSecurityException, IOException, HttpCodeException, NotFoundException {
//        //获取商户密钥
//        PrivateKey privateKey = getPrivateKey(getPrivateKeyUrl());
//
//        // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
//        CertificatesManager verifier = CertificatesManager.getInstance();
//        verifier.putMerchant(mchId, new WechatPay2Credentials(mchId,
//                new PrivateKeySigner(mchSerialNo, privateKey)), apiV3Key.getBytes(StandardCharsets.UTF_8));
//
//        // 初始化httpClient
//        return WechatPayHttpClientBuilder.create()
//                .withMerchant(mchId, mchSerialNo, privateKey)
//                .withValidator(new WechatPay2Validator(verifier.getVerifier(mchId))).build();
//    }
//}
