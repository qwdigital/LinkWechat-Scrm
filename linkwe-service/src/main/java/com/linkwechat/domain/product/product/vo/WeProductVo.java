package com.linkwechat.domain.product.product.vo;

import com.linkwechat.domain.media.WeMessageTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @date 2022年09月30日 11:51
 */
@ApiModel
@Data
public class WeProductVo {

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

    @ApiModelProperty("商品附件")
    private String attachments;
}
