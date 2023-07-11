package com.linkwechat.domain.leads.leads.query;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 线索退回表(LeadsReturnRecord)表实体类
 *
 * @author zhaoyijie
 * @since 2023-05-17 13:49:06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "leads_return_record")
public class LeadsReturnRecord {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 线索ID
     */
    private Long leadsId;

    /**
     * 退回人ID，系统自动退回设置0
     */
    private Long returnUserId;

    /**
     * 退回时间
     */
    private Date returnTime;

    /**
     * 退回原因
     */
    private String returnReason;

    /**
     * 是否最新一次退回
     */
    private Boolean isRecent;

    /**
     * 第几次退回
     */
    private Integer returnSort;

}

