package com.linkwechat.domain.leads.leads.vo;

import lombok.Data;

/**
 * 历史任务
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/20 14:27
 */
@Data
public class WeClientLeadsHistoryRecordVo extends WeClientLeadsGeneralVo {

    /**
     * 线索ID
     */
    private Long leadsId;
}
