package com.linkwechat.common.enums.leads.message;

/**
 * 线索中心消息通知类型
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/11 14:36
 */
public enum LeadsCenterMessageTypeEnum {

    /**
     * 管理员分配
     */
    ADMIN_ALLOCATION,

    /**
     * 超时回收
     */
    TIMEOUT_RECOVERY,

    /**
     * 管理员回收
     */
    ADMIN_RECOVERY,

    /**
     * 转接审批通过
     */
    TRANSFER_APPROVED,

    /**
     * 转接成功
     */
    TRANSFER_SUCCESS;

}
