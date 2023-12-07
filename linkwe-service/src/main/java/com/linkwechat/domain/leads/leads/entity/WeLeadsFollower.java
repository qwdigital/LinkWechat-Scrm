package com.linkwechat.domain.leads.leads.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

/**
 * 线索跟进人
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 15:50
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("we_leads_follower")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeLeadsFollower {

    /**
     * 主键Id
     */
    @TableId("id")
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
     * 跟进人企微Id
     */
    private String followerWeUserId;

    /**
     * 跟进人名称
     */
    private String followerName;

    /**
     * 跟进人所属部门Id
     */
    private Long deptId;

    /**
     * 跟进人所属部门名称
     */
    private String deptName;

    /**
     * 领取方式（0指定分配，1主动领取，2线索转接）
     */
    private Integer getType;

    /**
     * 跟进状态：0已领取，1跟进中，2已转化，3已退回
     */
    private Integer followerStatus;

    /**
     * 退回方式 0成员主动退回 1超时自动退回 2管理员强制回收 3离职退回
     */
    private Integer returnType;

    /**
     * 退回原因
     */
    private String returnReason;

    /**
     * 分配人id
     */
    private Long assignerId;

    /**
     * 分配人名称
     */
    private String assignerName;

    /**
     * 跟进开始时间
     */
    private Date followerStartTime;

    /**
     * 跟进结束时间
     */
    private Date followerEndTime;

    /**
     * 是否当前跟进人（0否，1是）
     */
    @TableField(value = "is_current_follower")
    private Integer isCurrentFollower;

    /**
     * 跟进时线索所处的公海Id
     */
    private Long seaId;

    /**
     * 是否最新跟进人（0否，1是）
     */
    @TableField(value = "is_latest")
    private Integer latest;
}
