package com.linkwechat.wecom.domain.vo;

import lombok.Data;

/**
 * 消息发送结果
 */
@Data
public class WeCustomerMessageResultVo {

    /**
     * 发送员工名称
     */
    private String userName;

    /**
     * 客户名称列表，通过`、`进行分隔
     */
    private String customers;

}
