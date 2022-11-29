package com.linkwechat.domain.product.order.query;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 下单
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/23 14:41
 */
@Data
public class WePlaceAnOrderQuery {

    /**
     * 产品Id
     */
    @NotNull(message = "产品Id必填")
    private Long productId;

    /**
     * 下单数量
     */
    @NotNull(message = "下单数量必填")
    private Integer productNum;

    /**
     * 付款总金额：单位（元,保留两位小数）
     */
    @NotBlank(message = "付款总金额必填")
    private String totalFee;

    /**
     * 订单联系人
     */
    @NotBlank(message = "订单联系人必填")
    private String contact;

    /**
     * 订单联系人电话
     */
    @NotBlank(message = "订单联系人电话必填")
    private String phone;

    /**
     * 订单联系人地址-省id
     */
    @NotBlank(message = "订单联系人地址-省id必填")
    private Long provinceId;

    /**
     * 订单联系人地址-市id
     */
    @NotBlank(message = "订单联系人地址-市id必填")
    private Long cityId;

    /**
     * 订单联系人地址-县id
     */
    @NotBlank(message = "订单联系人地址-县id必填")
    private Long areaId;

    /**
     * 订单联系人地址省市县
     */
    @NotBlank(message = "订单联系人地址省市县必填")
    private String area;

    /**
     * 订单联系人详细地址
     */
    @NotBlank(message = "订单联系人详细地址必填")
    private String address;

    /**
     * 客户openId
     */
    @NotBlank(message = "客户openId必填")
    private String openId;

    /**
     * 客户名称
     */
    @NotBlank(message = "客户名称必填")
    private String name;

    /**
     * 客户头像
     */
    private String avatar;

    /**
     * 客户类型 1微信，2企微
     */
    private Integer type;

    /**
     * 商品发送员工
     */
    @NotBlank(message = "商品发送员工必填")
    private String weUserId;


}
