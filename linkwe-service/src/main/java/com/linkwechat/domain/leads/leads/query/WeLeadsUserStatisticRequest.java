package com.linkwechat.domain.leads.leads.query;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 员工统计top5请求参数
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/19 17:54
 */
@Data
public class WeLeadsUserStatisticRequest {

    /**
     * 部门Id
     */
    private Long deptId;

    /**
     * 员工Id
     */
    private Long userId;


}
