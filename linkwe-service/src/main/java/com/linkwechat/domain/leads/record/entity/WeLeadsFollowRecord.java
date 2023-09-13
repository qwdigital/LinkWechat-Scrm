package com.linkwechat.domain.leads.record.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 线索跟进记录
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 18:35
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("we_leads_follow_record")
public class WeLeadsFollowRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 线索id
     */
    private Long weLeadsId;

    /**
     * 公海id
     */
    private Long seaId;

    /**
     * 跟进人记录表id
     */
    private Long followUserId;

    /**
     * 记录状态 0已领取 1跟进中 2已转化 3已退回
     */
    private Integer recordStatus;

    /**
     * 创建时间
     */
    private Date createTime;
}
