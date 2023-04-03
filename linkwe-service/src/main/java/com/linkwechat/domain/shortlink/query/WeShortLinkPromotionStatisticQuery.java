package com.linkwechat.domain.shortlink.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 短链推广折线图
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/19 15:14
 */
@ApiModel
@Data
public class WeShortLinkPromotionStatisticQuery {

    @NotNull(message = "短链推广Id必填")
    @ApiModelProperty(value = "短链推广Id")
    private Long promotionId;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("开始时间")
    private String beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("结束时间")
    private String endTime;
}
