package com.linkwechat.wecom.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * 客户所属员工相关信息
 */
@Data
public class CusertomerBelongUserInfo {
    //当前所属员工
    private String belongUserName;
    //首次添加员工
    private String userName;
    //首次添加时间
    private Date createTime;
}
