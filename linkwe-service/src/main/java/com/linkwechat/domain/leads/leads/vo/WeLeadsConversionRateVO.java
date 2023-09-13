package com.linkwechat.domain.leads.leads.vo;

import lombok.Data;

/**
 * 线索转化率
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/19 15:12
 */
@Data
public class WeLeadsConversionRateVO {

    /**
     * 员工名称
     */
    private String userName;

    /**
     * 转化率
     */
    private String rate;

}
