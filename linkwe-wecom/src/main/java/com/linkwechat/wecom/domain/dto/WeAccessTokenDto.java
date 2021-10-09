package com.linkwechat.wecom.domain.dto;

import lombok.Data;

/**
 * @description: accessToken相关
 * @author: HaoN
 * @create: 2020-08-27 15:54
 **/
@Data
public class WeAccessTokenDto extends WeResultDto {
    /**
     * 企业微信token
     */
    private String accessToken;

    /**
     * token  有效时间（秒）
     */
    private Long expiresIn;
}
