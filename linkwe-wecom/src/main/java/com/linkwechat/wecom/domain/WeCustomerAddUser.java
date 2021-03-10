package com.linkwechat.wecom.domain;

import lombok.Data;

import java.util.Date;

/**
 * @description: 客户添加人
 * @author: HaoN
 * @create: 2021-03-08 15:47
 **/
@Data
public class WeCustomerAddUser {
    //员工名称
    private String userName;
    //员工id
    private String userId;
    //员工头像
    private String headImageUrl;
    //创建时间
    private Date createTime;
}
