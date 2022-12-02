package com.linkwechat.domain.product.refund.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 退款订单
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/22 10:54
 */
@Data
public class WeProductOrderRefundVo {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 退款单号
     */
    private String refundNo;

    /**
     * 退款发起时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime refundTime;

    /**
     * 退款发起人Id
     */
    private String refundUserId;

    /**
     * 退款发起人姓名
     */
    private String refundUserName;

    /**
     * 退款备注
     */
    private String remark;

    /**
     * 退款金额
     */
    private String refundFee;

    /**
     * 退款状态
     */
    private Integer refundState;

    /**
     * 退款状态Str
     */
    private String refundStateStr;

}
