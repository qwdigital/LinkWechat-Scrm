package com.linkwechat.domain.leads.leads.query;

import lombok.Data;

import java.util.Date;

/**
 * 线索回收
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/12 10:03
 */
@Data
public class WeClientLeadsRecoveryMqParam {

    /**
     * 线索Id
     */
    private Long leadsId;

    /**
     * 公海Id
     */
    private Long seaId;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 第一条规则 1
     * 第二条规则 2
     * 第三条规则 3
     */
    private Integer type;

    /**
     * A框，截止时间
     */
    private Date ATime;

    /**
     * C框截止时间
     */
    private Date CTime;

    /**
     * D框，截止时间
     */
    private Date DTime;

    /**
     * 当前版本
     */
    private Integer version;
}
