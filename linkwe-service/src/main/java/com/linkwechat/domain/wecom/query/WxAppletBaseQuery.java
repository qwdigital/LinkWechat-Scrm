package com.linkwechat.domain.wecom.query;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author danmo
 * @Description 微信小程序请求基础入参
 * @date 2022/12/2 18:27
 **/
@ApiModel
@Data
public class WxAppletBaseQuery {

    /**
     * 企业id
     */
    public String accessToken;

}
