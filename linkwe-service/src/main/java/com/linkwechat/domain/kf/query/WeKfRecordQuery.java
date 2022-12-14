package com.linkwechat.domain.kf.query;

import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 咨询记录入参
 * @date 2022/2/6 21:41
 **/
@ApiModel
@Data
public class WeKfRecordQuery extends BaseEntity {

    @ApiModelProperty("客服账号ID")
    private String openKfId;

    @ApiModelProperty(value = "客户ID")
    private String externalUserId;

    @ApiModelProperty("客服场景值")
    private String scene;

    @ApiModelProperty("是否企业客户 0-是 1-否")
    private Integer isQyCustomer;

    @ApiModelProperty("员工ID")
    private String userId;

    @ApiModelProperty("连接池ID")
    private String poolId;
}
