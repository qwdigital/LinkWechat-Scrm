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
import java.util.Date;

/**
 * 商品订单表
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/21 18:02
 */
@ApiModel
@Data
@TableName("we_product_order")
public class WeProductOrder extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    @ApiModelProperty(value = "订单号")
    @TableField("order_no")
    private String orderNo;

    @ApiModelProperty(value = "商户单号")
    @TableField("mch_no")
    private String mchNo;

    @ApiModelProperty(value = "订单状态（1已完成，3已完成有退款）")
    @TableField("order_state")
    private Integer orderState;

    @ApiModelProperty(value = "付款总金额:单位分")
    @TableField("total_fee")
    private String totalFee;

    @ApiModelProperty(value = "交易时间")
    @TableField("pay_time")
    private Date payTime;

    @ApiModelProperty(value = "产品Id")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty(value = "购买数量")
    @TableField("product_num")
    private Integer productNum;

    @ApiModelProperty(value = "订单联系人")
    @TableField("contact")
    private String contact;

    @ApiModelProperty(value = "订单联系人电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "订单联系人详细地址")
    @TableField("address")
    private String address;

    @ApiModelProperty(value = "付款人的userid")
    @TableField("external_userid")
    private String externalUserid;

    @ApiModelProperty(value = "付款人名称")
    @TableField("external_name")
    private String externalName;

    @ApiModelProperty(value = "付款人头像")
    @TableField("external_avatar")
    private String externalAvatar;

    @ApiModelProperty(value = "外部联系人的类型，1微信用户，2企业微信用户")
    @TableField("external_type")
    private Integer externalType;

    @ApiModelProperty(value = "发送员工")
    @TableField("we_user_id")
    private String weUserId;

    @ApiModelProperty(value = "发送员工名称")
    @TableField("we_user_name")
    private String weUserName;

    @ApiModelProperty(value = "收款商户名称")
    @TableField("mch_name")
    private String mchName;

    @ApiModelProperty(value = "收款商户号")
    @TableField("mch_id")
    private String mchId;

    @ApiModelProperty(value = "是否删除:0有效,1删除")
    @TableField("del_flag")
    private Integer delFlag;

    @ApiModelProperty(value = "收款方式。0：在聊天中收款 1：收款码收款 2：在直播间收款 3：用产品图册收款")
    @TableField("payment_type")
    private Integer paymentType;

    @ApiModelProperty(value = "收款备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "退款总金额")
    @TableField("total_refund_fee")
    private String totalRefundFee;


}
