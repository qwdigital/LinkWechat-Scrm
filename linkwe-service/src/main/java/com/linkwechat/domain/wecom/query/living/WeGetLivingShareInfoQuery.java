package com.linkwechat.domain.wecom.query.living;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @description 直播接口入参
 * @date 2022/10/10 10:27
 **/
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class WeGetLivingShareInfoQuery extends WeLivingQuery {

    /**
     * “推广产品”直播观众跳转小程序商城时会在小程序path中带上ww_share_code=xxxxx参数，ww_share_code五分钟内有效
     */
    @ApiModelProperty("“推广产品”直播观众跳转小程序商城时会在小程序path中带上ww_share_code=xxxxx参数，ww_share_code五分钟内有效")
    private String ww_share_code;
}
