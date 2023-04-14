package com.linkwechat.domain.qr.query;

import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 活码列表入参
 * @date 2021/11/9 13:58
 **/
@ApiModel
@Data
public class WeLxQrCodeListQuery extends BaseEntity {


    @ApiModelProperty("活码Id")
    private Long qrId;

    @ApiModelProperty("活码名称")
    private String qrName;

    @ApiModelProperty(value = "拉新方式 1：红包 2：卡券")
    private Integer type;

    @ApiModelProperty("员工ID")
    private String qrUserIds;

    @ApiModelProperty(value = "渠道",hidden = true)
    private String state;

}
