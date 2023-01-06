package com.linkwechat.domain.product.product.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author danmo
 * @date 2022年09月30日 11:51
 */
@ApiModel
@Data
public class WeProductListVo {

    @ApiModelProperty("商品ID")
    private Long id;

    @ApiModelProperty("商品封面地址")
    private String picture;

    @ApiModelProperty("商品描述")
    private String describe;

    @ApiModelProperty("商品编码")
    private String productSn;

    @ApiModelProperty("商品价格")
    private String price;

    @ApiModelProperty("订单总数")
    private Integer orderNum;

    @ApiModelProperty("订单总金额")
    private String orderTotalAmount;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("最近更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
