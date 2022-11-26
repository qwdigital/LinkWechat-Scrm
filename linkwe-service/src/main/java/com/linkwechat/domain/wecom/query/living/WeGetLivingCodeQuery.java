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
public class WeGetLivingCodeQuery extends WeLivingQuery {

    @ApiModelProperty("微信用户的openid")
    private String openid;
}
