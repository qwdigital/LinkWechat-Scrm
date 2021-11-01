package com.linkwechat.common.config;/*
package com.linkwechat.common.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.List;

*/
/**
 * @author danmo
 * @description ElasticSearch 配置
 * @date 2020/12/4 9:59
 **//*


@Configuration
public class ElasticSearchConfig {
    */
/**
     * 协议
     *//*

    @Value("${elasticsearch.schema:http}")
    private String schema;

    */
/**
     * 集群地址，如果有多个用“,”隔开
     *//*

    @Value("${elasticsearch.address}")
    private String address;

    @Value("${elasticsearch.userName}")
    private String userName;

    @Value("${elasticsearch.password}")
    private String password;
    */
/**
     * 连接超时时间
     *//*

    @Value("${elasticsearch.connectTimeout:5000}")
    private int connectTimeout;

    */
/**
     * Socket 连接超时时间
     *//*

    @Value("${elasticsearch.socketTimeout:10000}")
    private int socketTimeout;

    */
/**
     * 获取连接的超时时间
     *//*

    @Value("${elasticsearch.connectionRequestTimeout:5000}")
    private int connectionRequestTimeout;

    */
/**
     * 最大连接数
     *//*

    @Value("${elasticsearch.maxConnectNum:100}")
    private int maxConnectNum;

    */
/**
     * 最大路由连接数
     *//*

    @Value("${elasticsearch.maxConnectPerRoute:100}")
    private int maxConnectPerRoute;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        // 拆分地址
        List<HttpHost> hostLists = new ArrayList<>();
        String[] hostList = address.split(",");
        for (String addr : hostList) {
            String host = addr.split(":")[0];
            String port = addr.split(":")[1];
            hostLists.add(new HttpHost(host, Integer.parseInt(port), schema));
        }
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));
        // 转换成 HttpHost 数组
        HttpHost[] httpHost = hostLists.toArray(new HttpHost[]{});
        // 构建连接对象
        RestClientBuilder builder = RestClient.builder(httpHost);
        // 异步连接延时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connectTimeout);
            requestConfigBuilder.setSocketTimeout(socketTimeout);
            requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeout);
            return requestConfigBuilder;
        });
        // 异步连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(maxConnectNum);
            httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            return httpClientBuilder;
        });
        return new RestHighLevelClient(builder);

    }
}*/
