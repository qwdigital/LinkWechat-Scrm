package com.linkwechat.domain.leads.leads.query;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 线索绑定客户请求参数
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/18 17:47
 */
@Data
public class WeLeadsBindCustomerRequest {

    /**
     * 线索ID
     */
    @NotNull(message = "线索ID必填")
    private Long leadsId;

    /**
     * 客户ID
     */
    @NotNull(message = "客户ID必填")
    private Long customerId;

    /**
     * 客户外部联系人Id
     */
    @NotBlank(message = "客户外部联系人Id")
    private String externalUserid;

}
