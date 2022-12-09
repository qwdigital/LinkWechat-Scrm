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
 * 商品订单每日统计表
 * </p>
 *
 * @author WangYX
 * @since 2022-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("we_product_day_statistics")
public class WeProductDayStatistics extends Model<WeProductDayStatistics> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单总数
     */
    private Integer dayOrderTotalNum;

    /**
     * 订单总金额
     */
    private String dayOrderTotalFee;

    /**
     * 退款总额
     */
    private String dayRefundTotalFee;

    /**
     * 净收入
     */
    private String dayNetIncome;

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
