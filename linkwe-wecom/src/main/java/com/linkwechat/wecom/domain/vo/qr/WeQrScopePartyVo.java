package com.linkwechat.wecom.domain.vo.qr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 活码排期部门信息
 * @date 2021/11/8 0:19
 **/
@ApiModel
@Data
public class WeQrScopePartyVo {

    @ApiModelProperty(value = "部门id")
    private String party;

    @ApiModelProperty(value = "部门名称")
    private String partyName;
}
