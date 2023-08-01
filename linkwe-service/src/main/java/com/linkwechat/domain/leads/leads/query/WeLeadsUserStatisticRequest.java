package com.linkwechat.domain.leads.leads.query;

import lombok.Data;

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
     * 部门Id集合，逗号分隔
     */
    private String deptIds;

    /**
     * 员工Id集合,逗号分隔
     */
    private String userIds;


}
