package com.linkwechat.domain.product.product.vo;

import lombok.Data;

/**
 * 员工订单TOP5
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/22 13:33
 */
@Data
public class WeUserOrderTop5Vo {

    /**
     * 员工Id
     */
    private String weUserId;

    /**
     * 员工名称
     */
    private String weUserName;

    /**
     * 订单总数
     */
    private Integer orderNum;

    /**
     * 订单金额
     */
    private String totalFee;

}
