package com.linkwechat.wecom.domain.query.qr;

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
public class WeQrCodeListQuery extends BaseEntity {



    @ApiModelProperty("活码Id")
    private Long qrId;

    @ApiModelProperty("分组id")
    private String groupId;

    @ApiModelProperty("活码名称")
    private String qrName;

    @ApiModelProperty("员工名称")
    private String qrUserName;

}
