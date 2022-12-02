package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品订单统计表
 * </p>
 *
 * @author WangYX
 * @since 2022-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("we_product_statistics")
public class WeProductStatistics extends Model<WeProductStatistics> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单总数
     */
    private Integer orderTotalNum;

    /**
     * 订单总金额：单位分
     */
    private String orderTotalFee;

    /**
     * 退款总额：单位分
     */
    private String refundTotalFee;

    /**
     * 净收入：单位分
     */
    private String netIncome;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除:0有效,1删除
     */
    private Integer delFlag;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
