package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品信息表(WeProduct)
 *
 * @author danmo
 * @since 2022-09-30 11:36:06
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_product")
public class WeProduct extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    @ApiModelProperty(value = "商品ID")
    @TableField("product_id")
    private String productId;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    @TableField("picture")
    private String picture;


    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @TableField("`describe`")
    private String describe;


    /**
     * 商品的价格，单位为分；最大不超过5万元
     */
    @ApiModelProperty(value = "商品的价格，单位为分；最大不超过5万元")
    @TableField("price")
    private String price;


    /**
     * 商品编码；只能输入数字和字母
     */
    @ApiModelProperty(value = "商品编码；只能输入数字和字母")
    @TableField("product_sn")
    private String productSn;

    /**
     * 商品附件
     */
    @ApiModelProperty(value = "商品附件")
    @TableField("attachments")
    private String attachments;


    /**
     * 是否删除:0有效,1删除
     */
    @ApiModelProperty(value = "是否删除:0有效,1删除")
    @TableField("del_flag")
    private Integer delFlag;
}
