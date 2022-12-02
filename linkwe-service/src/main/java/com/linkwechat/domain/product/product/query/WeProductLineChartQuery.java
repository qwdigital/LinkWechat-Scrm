package com.linkwechat.domain.product.product.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 商品折线图查询
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/22 11:40
 */
@Data
public class WeProductLineChartQuery {

    /**
     * 查询时间类型
     * customization 自定义 当为自定义类型时，开始时间和结束时间为必填
     * week 周
     * month 月
     */
    @ApiModelProperty(value = "查询时间类型")
    private String type;

    /**
     * 问卷id
     */
    @ApiModelProperty(value = "商品Id")
    @NotNull(message = "商品Id不能为空")
    private Long productId;

    /**
     * 开始时间:yyyy-MM-dd
     */
    @ApiModelProperty("开始时间")
    private String startDate;

    /**
     * 结束时间:yyyy-MM-dd
     */
    @ApiModelProperty("结束时间")
    private String endDate;
}
