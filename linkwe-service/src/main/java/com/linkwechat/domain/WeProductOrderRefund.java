package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品订单退款表
 * </p>
 *
 * @author WangYX
 * @since 2022-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("we_product_order_refund")
public class WeProductOrderRefund extends Model<WeProductOrderRefund> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人id
     */
    private Long createById;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新人id
     */
    private Long updateById;

    /**
     * 是否删除:0有效,1删除
     */
    private Integer delFlag;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
