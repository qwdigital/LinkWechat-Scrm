package com.linkwechat.domain.leads.leads.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 员工跟进数量统计
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 16:56
 */
@Data
@Builder
public class WeLeadsFollowerNumVO {

    @ApiModelProperty(value = "员工每日领取上限")
    private Long maxClaim;

    @ApiModelProperty(value = "员工客户存量上限")
    private Long stockMaxClaim;

}
