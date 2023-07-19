package com.linkwechat.domain.leads.leads.query;

import lombok.Data;

/**
 * 线索基础请求类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/04 16:57
 */
@Data
public class WeLeadsBaseRequest {

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 线索状态(0待分配，1跟进中，2已上门，3已退回)
     */
    private Integer leadsStatus;

    /**
     * 所属公海
     */
    private Long seaId;

}
