package com.linkwechat.domain.taggroup.vo;

import lombok.Data;

@Data
public class WePresTagGroupTaskTabCountVo {
    //送达客户数
    private int touchWeCustomerNumber;

    //进群客户总数
    private int joinGroupCustomerNumber;

    //今日添加客户总数
    private int tdTouchWeCustomerNumber;


    //今日进群客户总数
    private int tdJoinGroupCustomerNumber;
}
