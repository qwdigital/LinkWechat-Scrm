package com.linkwechat.domain.leads.leads.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 线索自动回收
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 10:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "we_leads_auto_recovery")
public class WeLeadsAutoRecovery extends BaseEntity {

    /**
     * 主键Id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 线索Id
     */
    private Long leadsId;

    /**
     * 跟进人id
     */
    private Long followerId;

    /**
     * 自动回收类型 0超时未跟进自动回收 1
     */
    private Integer type;

    /**
     * 回收时间
     */
    private Date recoveryTime;

    /**
     * 执行状态 0待执行 1已执行 2已取消
     */
    private Integer executingState;

    /**
     * 回收原因
     */
    private Integer recoveryReason;

    /**
     * 删除标识
     */
    private Integer delFlag;
}
