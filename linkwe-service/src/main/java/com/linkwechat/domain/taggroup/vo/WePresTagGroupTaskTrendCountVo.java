package com.linkwechat.domain.taggroup.vo;

import lombok.Data;

@Data
public class WePresTagGroupTaskTrendCountVo {
    //日期
    private  String date;


    //送达客户数
    private int touchWeCustomerNumber;

    //进群客户总数
    private int joinGroupCustomerNumber;
}
