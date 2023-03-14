package com.linkwechat.controller;


import com.linkwechat.wechatpay.WechatPayService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信支付卡券
 *
 * @author danmo
 * @date 2023年03月11日 16:57
 */

@Api(tags = "微信支付卡券")
@Slf4j
@RestController
@RequestMapping("/coupon")
public class WeCouponController {


    @Autowired
    private WechatPayService wechatPayService;


    /**
     * @throws Exception
     */
    @GetMapping("/getCouponList")
    public void getCouponList() throws Exception {
        String result = wechatPayService.sendGetRequest("");

    }
    /*
     *//** 商户号 *//*
    public static String mchId = "1613220424";
    *//** 商户API私钥路径 *//*
    public static String privateKey = "D:\\Windows\\system32\\WXCertUtil\\cert\\apiclient_key.pem";
    *//** 商户证书序列号 *//*
    public static String mchSerialNo = "799B9A9B7361FD0D54ABCC31DDDD2896313C0055";
    *//** 商户APIV3密钥 *//*
    public static String apiV3Key = "3b141e1a3c6245038681A504c5a6Qwas";

    public static void main(String[] args) throws Exception {
        GetStocksList();
   }

    public static void GetStocksList() throws Exception{

        // 加载商户私钥（privateKey：私钥字符串）
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new FileInputStream(ResourceUtils.getFile(privateKey)));

        // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
        CertificatesManager verifier = CertificatesManager.getInstance();
        verifier.putMerchant(mchId, new WechatPay2Credentials(mchId,
                new PrivateKeySigner(mchSerialNo, merchantPrivateKey)), apiV3Key.getBytes(StandardCharsets.UTF_8));

        // 初始化httpClient
        CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, mchSerialNo, merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier.getVerifier(mchId))).build();
        //请求URL
        HttpGet httpGet = new HttpGet("https://api.mch.weixin.qq.com/v3/marketing/favor/stocks?offset=0&limit=10&stock_creator_mchid=1613220424");
        httpGet.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) { //处理成功
                System.out.println("success,return body = " + EntityUtils.toString(response.getEntity()));
            } else if (statusCode == 204) { //处理成功，无返回Body
                System.out.println("success");
            } else {
                System.out.println("failed,resp code = " + statusCode+ ",return body = " + EntityUtils.toString(response.getEntity()));
                throw new IOException("request failed");
            }
        } finally {
            httpClient.close();
            response.close();
        }
    }*/
}
