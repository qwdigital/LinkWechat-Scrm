package com.linkwechat.domain.product.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.product.refund.vo.WeProductOrderRefundVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 商品订单
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/28 17:51
 */
@Data
public class WeProductOrderWareVo {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "产品Id")
    private Long productId;

    @ApiModelProperty(value = "付款人的userid")
    private String externalUserid;

    @ApiModelProperty(value = "商品描述")
    private String describe;

    @ApiModelProperty(value = "付款人名称")
    private String externalName;

    @ApiModelProperty(value = "订单联系人")
    private String contact;

    @ApiModelProperty(value = "订单联系人详细地址")
    private String address;

    @ApiModelProperty(value = "订单联系人电话")
    private String phone;

    @ApiModelProperty(value = "购买数量")
    private Integer productNum;

    @ApiModelProperty(value = "付款总金额")
    private String totalFee;

    @ApiModelProperty(value = "收款员工名称")
    private String weUserName;

    @ApiModelProperty(value = "收款商户名称")
    private String mchName;

    @ApiModelProperty(value = "收款商户号")
    private String mchId;

    @ApiModelProperty(value = "订单状态（1已完成，2已完成有退款）")
    private Integer orderState;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "交易时间")
    private Date payTime;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "商户单号")
    private String mchNo;

    @ApiModelProperty(value = "付款人头像")
    private String externalAvatar;

    @ApiModelProperty(value = "外部联系人的类型，1微信用户，2企业微信用户")
    private Integer externalType;

    @ApiModelProperty(value = "收款员工")
    private String weUserId;

    @ApiModelProperty(value = "商品封面")
    private String picture;

    /**
     * 退款订单
     */
    private List<WeProductOrderRefundVo> refunds;


}
