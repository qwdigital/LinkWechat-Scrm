package com.linkwechat.wecom.domain.weixin.dto;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 授权用户信息
 * @date 2021/4/5 16:12
 **/
@ApiModel
@Data
public class WxAuthUserInfoDto extends WxBaseResultDto{
    @ApiModelProperty("用户的唯一标识")
    private String openId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户的性别，值为1时是男性，值为2时是女性，值为0时是未知")
    private Integer sex;

    @ApiModelProperty("用户个人资料填写的省份")
    private String province;

    @ApiModelProperty("普通用户个人资料填写的城市")
    private String city;

    @ApiModelProperty("国家，如中国为CN")
    private String country;

    @ApiModelProperty("用户头像")
    private String headImgUrl;

    @ApiModelProperty("用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）")
    private JSONArray privilege;

    @ApiModelProperty("只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。")
    private String unionId;
}
