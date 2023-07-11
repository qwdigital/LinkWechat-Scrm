package com.linkwechat.domain.leads.leads.query;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 成员主动退回
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/19 15:02
 */
@Data
public class WeClientLeadsUserReturnParam {

    /**
     * 线索Id
     */
    @NotNull(message = "线索Id必填")
    private Long leadsId;

    /**
     * 退回原因
     */
    @NotNull(message = "退回原因必填")
    private String reason;

    /**
     * 退回备注
     */
    private String remark;

}
