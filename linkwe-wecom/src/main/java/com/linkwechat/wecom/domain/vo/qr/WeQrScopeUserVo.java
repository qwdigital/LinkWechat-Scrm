package com.linkwechat.wecom.domain.vo.qr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 活码排期员工信息
 * @date 2021/11/8 0:18
 **/
@ApiModel
@Data
public class WeQrScopeUserVo {
    @ApiModelProperty(value = "员工id")
    private String userId;

    @ApiModelProperty(value = "员工姓名")
    private String userName;
}
