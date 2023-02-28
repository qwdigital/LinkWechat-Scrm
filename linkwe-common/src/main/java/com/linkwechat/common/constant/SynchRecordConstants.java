package com.linkwechat.common.constant;

import lombok.Data;

@Data
public class SynchRecordConstants {

    public static final int SYNCH_CUSTOMER=1;//客户同步
    public static final int SYNCH_MAIL_LIST=2;//通讯录同步(组织架构和员工)
    public static final int SYNCH_CUSTOMER_GROUP=3;//客户群同步
    public static final int SYNCH_CUSTOMER_PERSON_MOMENTS=4;//个人朋友圈
    public static final int SYNCH_CUSTOMER_ENTERPRISE_MOMENTS=5;//企业朋友圈
    public static final int SYNCH_CUSTOMER_TAG=6;//企业标签同步
    public static final int SYNCH_MOMENTS_INTERACTE=7;//同步指定员工个人朋友圈互动情况
    public static final int SYNCH_LIVE=8;//直播同步

    public static final int SYNCH_LEAVE_USER=9;//离职分配
}

