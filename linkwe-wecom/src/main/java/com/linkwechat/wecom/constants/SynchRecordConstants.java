package com.linkwechat.wecom.constants;

import lombok.Data;

@Data
public class SynchRecordConstants {

    public static final int SYNCH_CUSTOMER=1;//客户同步
    public static final int SYNCH_MAIL_LIST=2;//通讯录同步(组织架构和员工)
    public static final int SYNCH_CUSTOMER_GROUP=3;//客户群同步
}
