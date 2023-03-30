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
 * 拉新活码领取记录表(WeLxQrCodeLog)
 *
 * @author danmo
 * @since 2023-03-16 16:19:03
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_lx_qr_code_log")
public class WeLxQrCodeLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 活码ID
     */
    @ApiModelProperty(value = "活码ID")
    @TableField("qr_id")
    private Long qrId;


    /**
     * 拉新方式 1：红包 2：卡券
     */
    @ApiModelProperty(value = "拉新方式 1：红包 2：卡券")
    @TableField("type")
    private Integer type;


    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    @TableField("order_id")
    private String orderId;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    @TableField("amount")
    private Integer amount;


    /**
     * unionID
     */
    @ApiModelProperty(value = "unionID")
    @TableField("union_id")
    private String unionId;

    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    @TableField("del_flag")
    private Integer delFlag;
}
