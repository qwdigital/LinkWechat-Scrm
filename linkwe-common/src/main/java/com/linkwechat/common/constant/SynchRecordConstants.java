package com.linkwechat.common.constant;

import lombok.Data;

/**
 * 同步记录常量
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/06 18:40
 */
@Data
public class SynchRecordConstants {

    /**
     * 客户同步
     */
    public static final int SYNCH_CUSTOMER = 1;

    /**
     * 通讯录同步(组织架构和员工)
     */
    public static final int SYNCH_MAIL_LIST = 2;

    /**
     * 客户群同步
     */
    public static final int SYNCH_CUSTOMER_GROUP = 3;

    /**
     * 朋友圈
     */
    public static final int SYNCH_MOMENTS = 4;

    /**
     * 企业标签同步
     */
    public static final int SYNCH_CUSTOMER_TAG = 6;

    /**
     * 同步指定员工个人朋友圈互动情况
     */
    public static final int SYNCH_MOMENTS_INTERACTE = 7;

    /**
     * 直播同步
     */
    public static final int SYNCH_LIVE = 8;

    /**
     * 离职分配
     */
    public static final int SYNCH_LEAVE_USER = 9;


    /**
     * 商品图册订单
     */
    public static final int SYNCH_SPTC_ORDER=10;
}

