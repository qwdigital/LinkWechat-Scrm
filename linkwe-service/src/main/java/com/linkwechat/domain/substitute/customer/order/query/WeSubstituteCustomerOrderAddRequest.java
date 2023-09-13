package com.linkwechat.domain.substitute.customer.order.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.substitute.customer.order.vo.WeSubstituteCustomerOrderCataloguePropertyValueVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 代客下单-订单
 * </p>
 *
 * @author WangYX
 * @since 2023-08-03
 */
@Data
public class WeSubstituteCustomerOrderAddRequest {

    /**
     * 购买人
     */
    @ApiModelProperty("购买人")
    private String purchaser;

    /**
     * 联系方式
     */
    @ApiModelProperty("联系方式")
    private String phone;

    /**
     * 订单来源
     */
    @ApiModelProperty("订单来源")
    private String source;

    /**
     * 下单时间
     */
    @ApiModelProperty("下单时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime orderTime;

    /**
     * 归属部门
     */
    @ApiModelProperty("归属部门")
    private Long deptId;

    /**
     * 归属员工
     */
    @ApiModelProperty("归属员工")
    private Long userId;

    /**
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private String orderStatus;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String productName;

    /**
     * 商品图片
     */
    @ApiModelProperty("商品图片")
    private String productUrl;

    /**
     * 商品单价
     */
    @ApiModelProperty("商品单价")
    private BigDecimal productUnitPrice;

    /**
     * 商品数量
     */
    @ApiModelProperty("商品数量")
    private Integer amount;

    /**
     * 付款总价
     */
    @ApiModelProperty("付款总价")
    private BigDecimal totalPrice;

    /**
     * 付款折扣
     */
    @ApiModelProperty("付款折扣")
    private String discount;

    /**
     * 折扣金额
     */
    @ApiModelProperty("折扣金额")
    private BigDecimal discountAmount;

    /**
     * 实际付款
     */
    @ApiModelProperty("实际付款")
    private BigDecimal actualPayment;

    /**
     * 回款方式
     */
    @ApiModelProperty("回款方式")
    private String returnedMoneyType;

    /**
     * 回款金额
     */
    @ApiModelProperty("回款金额")
    private BigDecimal returnedMoney;

    /**
     * 回款日期
     */
    @ApiModelProperty("回款日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime returnedDate;

    /**
     * 打款人
     */
    @ApiModelProperty("打款人")
    private String payer;

    /**
     * 回款凭证
     */
    @ApiModelProperty("回款凭证")
    private String returnedReceipt;

    /**
     * 状态：0暂存 1完成
     */
    @ApiModelProperty("状态：0暂存 1完成")
    private Integer status;

    /**
     * 外部联系人的userid
     */
    @ApiModelProperty("外部联系人的userid")
    private String externalUserid;

    /**
     * 自定义属性
     */
    @ApiModelProperty("自定义属性")
    private List<WeSubstituteCustomerOrderCataloguePropertyValueVO> customs;
}
