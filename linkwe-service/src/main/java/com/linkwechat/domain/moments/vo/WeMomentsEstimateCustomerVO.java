package com.linkwechat.domain.moments.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 预估朋友圈可见客户
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/03 10:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WeMomentsEstimateCustomerVO {


    /**
     * 主键id
     */
    private Long id;

    /**
     * 朋友圈任务id
     */
    private Long momentsTaskId;

    /**
     * 员工id
     */
    private Long userId;

    /**
     * 企微员工id
     */
    private String weUserId;

    /**
     * 员工名称
     */
    private String userName;

    /**
     * 客户id
     */
    private String externalUserid;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 送达状态 0已送达 1未送达
     */
    private Integer deliveryStatus;

}
