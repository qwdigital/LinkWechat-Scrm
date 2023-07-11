package com.linkwechat.domain.leads.leads.query;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 线索分配表(LeadsAllocationRecord)表实体类
 *
 * @author zhaoyijie
 * @since 2023-05-17 13:49:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "leads_allocation_record")
public class LeadsAllocationRecord {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 线索ID
     */
    private Long leadsId;

    /**
     * 分配人ID
     */
    private Long allocateUserId;

    /**
     * 分配时间
     */
    private Date allocateTime;

    /**
     * 接收人ID
     */
    private Long receivedUserId;

    /**
     * 是否最新一次分配
     */
    private Boolean isRecent;

    /**
     * 第几次分配
     */
    private Integer allocateSort;

    /**
     * 分配记录的版本，发生线索退回|线索转接，版本+1
     */
    private Integer allocateVersion;

    /**
     * 领取方式 0 成员主动领取 1 管理员主动分配 2 被线索转移 3 指定分配
     */
    private Integer claimType;

}

