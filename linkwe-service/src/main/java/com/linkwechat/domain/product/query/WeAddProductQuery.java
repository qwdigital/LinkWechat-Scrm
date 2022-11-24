package com.linkwechat.domain.product.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author danmo
 * @date 2022年09月30日 11:51
 */
@ApiModel
@Data
public class WeAddProductQuery {

    @NotBlank(message = "商品封面不能为空")
    @ApiModelProperty("商品封面地址")
    private String picture;

    @NotBlank(message = "商品描述不能为空")
    @ApiModelProperty("商品描述")
    private String describe;

    @NotBlank(message = "商品价格不能为空")
    @ApiModelProperty("商品价格")
    private String price;

    @ApiModelProperty("商品附件最少1个最多8个")
    private String attachments;

    @ApiModelProperty(value = "商品编码", hidden = true)
    private String productSn;
}
