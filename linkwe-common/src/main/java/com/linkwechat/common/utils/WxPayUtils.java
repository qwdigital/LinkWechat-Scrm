package com.linkwechat.common.utils;

import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.common.utils.sign.Md5Utils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.*;

public class WxPayUtils {

//    //普通红包url
//    private static String COMMONREDRNVELOPEURL="https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
//
//    //裂变红包url
//    private static String FISSIONREDRNVELOPEURL="https://api.mch.weixin.qq.com/mmpaymkttransfers/sendgroupredpack";

    //零钱到付
    private static  String PROMOTION_URL="https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";


    /**
     * 支付，带证书的请求
     */
    public static String doPostRedEnvelope(String postDataXML,String certP12Url,String pkcs12)
            throws Exception {

        // 指定读取证书格式为PKCS12
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        // 读取本机存放的PKCS12证书文件
        InputStream instream = FileUtils.getInputStreamByUrl(certP12Url);
        try {
            //指定PKCS12的密码
            keyStore.load(instream, pkcs12.toCharArray());
        } finally {
            instream.close();
        }
        //指定TLS版本
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore,
                pkcs12.toCharArray()).build();
        //设置httpclient的SSLSocketFactory
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext, new String[]{"TLSv1.2"},null,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        StringBuffer sb = new StringBuffer();
        try {
            HttpPost httpPost = new HttpPost(PROMOTION_URL);
            StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
            httpPost.setEntity(postEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(entity.getContent(), "UTF-8"));
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    sb.append(inputLine);
                }
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return sb.toString();
    }

    /**
     * 生成红包接口签名
     *
     * @param parametersMap 入参
     * @return
     */
    public static String redEnvelopeSign(Map<String, String> parametersMap, String privateKey) {
        List<String> emptKeys = new ArrayList<>();
        parametersMap.forEach((k, v) -> {
            if (StringUtils.isEmpty(v)) {
                emptKeys.add(k);
            }
        });
        //移除值为空的键值对
        emptKeys.forEach(k -> {
            parametersMap.remove(k);
        });
        List<Map.Entry<String, String>> infoIds =
                new ArrayList<>(parametersMap.entrySet());
        //将集合内非空参数值的参数按照参数名ASCII码从小到大排序（字典序）
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });
        String strA = infoIds.toString();
        //入参格式化为URL键值对的格式
        strA = strA.substring(1, strA.length() - 1);
        strA = strA.replace(", ", "&");
        //添加腾讯为商户平台设置的密钥key
        strA = strA + "&key=" + privateKey;
        System.out.println(strA);
        String sign= Md5Utils.getMD532Str(strA).toUpperCase();
        return sign;
    }





    public static String getMchBillNo() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate=sdf.format(new Date());
        String result="";
        Random random=new Random();
        for(int i=0;i<3;i++){
            result+=random.nextInt(10);
        }
        return newDate+result;
    }



}
