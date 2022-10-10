package com.linkwechat.common.utils;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class MapUtils {
    /**
     * http://lbsyun.baidu.com/apiconsole/key
     * <百度开发者>用户申请注册的key，自v2开始参数修改为“ak”，之前版本参数为“key” 申请ak
     */
    final static String AK = "edGc5mIugVxx7lwUx9YpraKeWmExG64o";

    /**
     * 经度
     */
   public final static String lng = "lng";

    /**
     * 纬度
     */
   public final static String lat = "lat";


    /**
     * 地理编码 URL
     */
    final static String ADDRESS_TO_LONGITUDEA_URL = "http://api.map.baidu.com/geocoding/v3/?output=json&location=showLocation";

    /**
     * 地理编码
     * @param address  (广东省广州市黄埔区)
     *         详细的位置信息
     * @return
     */
    public  Map<String,String> addressTolongitudea(String address) {

        Map<String,String> lMap=new HashMap<>();

        if(StringUtils.isBlank(address)){

            return null;
        }

        String url = ADDRESS_TO_LONGITUDEA_URL + "&ak=" + AK + "&address="+ address;
        HttpClient client = HttpClients.createDefault(); // 创建默认http连接
        HttpPost post = new HttpPost(url);// 创建一个post请求
        try {
            HttpResponse response = client.execute(post);// 用http连接去执行get请求并且获得http响应
            HttpEntity entity = response.getEntity();// 从response中取到响实体
            String html = EntityUtils.toString(entity);// 把响应实体转成文本
            Result result = JSON.parseObject(html, ReturnLocationBean.class).getResult();
            if(null != result){
                Location location = result.getLocation();
                if(null != location){
                    lMap.put(lng,location.getLng());
                    lMap.put(lat,location.getLat());
                }
            }
            // JSON转对象
            return  lMap;
        } catch (Exception e) {
            return null;
        }

    }


    @Data
    public static class ReturnLocationBean{
         private Integer status;
         private Result result;
    }

    @Data
    public static class Result{
        private  Location location;
    }

    @Data
    public static class Location{
        private String lng;
        private String lat;
    }

}
