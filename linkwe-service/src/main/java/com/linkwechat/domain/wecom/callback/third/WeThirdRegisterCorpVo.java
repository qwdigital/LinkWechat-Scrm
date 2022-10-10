package com.linkwechat.domain.wecom.callback.third;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 注册企业
 * @date 2022/3/7 22:00
 **/
@ApiModel
@Data
public class WeThirdRegisterCorpVo extends WeThirdBackBaseVo{

    @ApiModelProperty("服务商corpid")
    private String ServiceCorpId;

    @ApiModelProperty("创建企业对应的注册码")
    private String RegisterCode;

    @ApiModelProperty("推广包ID")
    private String TemplateId;

    @ApiModelProperty("创建企业对应的注册码")
    private ContactSyncVo ContactSync;

    @ApiModelProperty("授权管理员的信息")
    private AuthUserInfoVo AuthUserInfo;


    @Data
    public class ContactSyncVo{
        private String  AccessToken;
        private Integer  ExpiresIn;
    }

    @Data
    public class AuthUserInfoVo{
        private String  UserId;
    }
}
