package com.linkwechat.domain.leads.record.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 线索跟进记录
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/18 14:05
 */
@Data
public class WeLeadsFollowRecordVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 线索id
     */
    private Long weLeadsId;

    /**
     * 记录状态 0已领取 1跟进中 2已转化 3已退回
     */
    private Integer recordStatus;

    /**
     * 记录状态 0已领取 1跟进中 2已转化 3已退回
     */
    private String recordStatusFullName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 跟进记录内容
     */
    private List<WeLeadsFollowRecordContentVO> contents;
}

