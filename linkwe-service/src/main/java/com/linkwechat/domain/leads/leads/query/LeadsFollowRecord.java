package com.linkwechat.domain.leads.leads.query;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 线索跟进表(LeadsFollowRecord)表实体类
 *
 * @author zhaoyijie
 * @since 2023-05-17 13:49:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "leads_follow_record")
public class LeadsFollowRecord {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 跟进记录ID
     */
    private Long followId;

    /**
     * 线索ID
     */
    private Long leadsId;

    /**
     * 跟进人ID
     */
    private Long followUserId;

    /**
     * 跟进时间
     */
    private Date followTime;

    /**
     * 当前版本第几次跟进
     */
    private Integer followSort;

    /**
     * 是否是最新版本跟进
     */
    private Boolean isRecent;

    private Boolean isVersion ;

    /**
     * 分配记录的版本，发生线索退回|线索转接，版本+1
     */
    private Integer followVersion;

}

