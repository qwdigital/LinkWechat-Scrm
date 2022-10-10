package com.linkwechat.domain.wecom.query;

import lombok.Data;

/**
 * @author danmo
 * @Description 获取凭证
 * @date 2021/12/2 18:27
 **/
@Data
public class WeProvideTokenQuery{

    /**
     * 服务商的corpid
     */
    private String corpid;

    /**
     * 服务商的secret
     */
    private String provider_secret;


}
