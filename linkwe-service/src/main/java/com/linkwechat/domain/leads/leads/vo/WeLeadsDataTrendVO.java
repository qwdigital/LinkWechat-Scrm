package com.linkwechat.domain.leads.leads.vo;

import lombok.Data;

/**
 * 线索分析-数据趋势
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/19 11:09
 */
@Data
public class WeLeadsDataTrendVO {

    /**
     * 日期
     */
    private String dateStr;

    /**
     * 导入线索数
     */
    private Integer leadsNum;

    /**
     * 添加客户数
     */
    private Integer customerNum;

    /**
     * 线索日跟进人数
     */
    private Integer followNum;
}
