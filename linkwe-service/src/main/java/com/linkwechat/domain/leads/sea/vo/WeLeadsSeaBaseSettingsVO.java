package com.linkwechat.domain.leads.sea.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 公海基础配置
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 16:35
 */
@Data
public class WeLeadsSeaBaseSettingsVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "员工领取上限")
    private Integer maxClaim;

    @ApiModelProperty(value = "员工客户存量上限")
    private Integer stockMaxClaim;

}

