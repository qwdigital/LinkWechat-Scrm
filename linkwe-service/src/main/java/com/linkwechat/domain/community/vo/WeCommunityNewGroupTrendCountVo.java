package com.linkwechat.domain.community.vo;

import lombok.Data;

@Data
public class WeCommunityNewGroupTrendCountVo {


    //日期
    private  String date;


    //添加客户总数
    private int addCustomerNumber;


    //进群客户总数
    private int joinGroupCustomerNumber;


}
