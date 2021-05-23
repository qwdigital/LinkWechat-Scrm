package com.linkwechat.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Kewen
 */
@Component
//@ConfigurationProperties(prefix = "cos")
@Data
public class CosConfig {

    private String secretId;

    private String secretKey;

    private String region;

    private String bucketName;

    private String cosImgUrlPrefix;

}
