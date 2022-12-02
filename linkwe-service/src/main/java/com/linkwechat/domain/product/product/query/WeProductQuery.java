package com.linkwechat.domain.product.product.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @date 2022年09月30日 11:51
 */
@ApiModel
@Data
public class WeProductQuery {

    @ApiModelProperty("商品描述")
    private String name;

    @ApiModelProperty("商品价格")
    private String price;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private String beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private String endTime;

}
