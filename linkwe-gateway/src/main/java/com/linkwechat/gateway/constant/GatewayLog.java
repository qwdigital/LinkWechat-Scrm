package com.linkwechat.gateway.constant;

import lombok.Data;

import java.util.Date;

/**
 * 日志请求体
 * @author danmo
 * @date 2023年03月10日 18:49
 */
@Data
public class GatewayLog {
    /**访问实例*/
    private String targetServer;
    /**请求路径*/
    private String requestPath;
    /**请求方法*/
    private String requestMethod;
    /**协议 */
    private String schema;
    /**请求体*/
    private String requestBody;
    /**响应体*/
    private String responseData;
    /**请求ip*/
    private String ip;
    /**请求时间*/
    private Date requestTime;
    /**响应时间*/
    private Date responseTime;
    /**执行时间*/
    private long executeTime;
}
