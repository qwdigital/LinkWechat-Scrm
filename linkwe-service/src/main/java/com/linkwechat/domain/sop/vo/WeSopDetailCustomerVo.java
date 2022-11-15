package com.linkwechat.domain.sop.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * sop统计列表客户列表
 */
@Data
public class WeSopDetailCustomerVo {
    //客户名称
    private String customerName;
    //外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户
    private Integer customerType;
    //客户企业名称
    private String corpName;
    //头像
    private String  avatar;
    //客户id
    private String externalUserid;
    //sop执行的状态(1:进行中;2:提前结束;3:正常结束;4:异常结束)
    private Integer executeState;
    //跟进员工
    private String userName;
    //岗位名称
    private String position;
    //所属部门
    private String deptName;
    //进入SOP时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    //结束SOP时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date executeEndTime;
    //sop主键
    private String sopBaseId;
    //执行效率
    private String efficiency;
    //企业员工的id
    private String weUserId;

    private String  executeTargetId;

    /**
     * 外部联系人性别 0-未知 1-男性 2-女性
     */
    private Integer gender;

}
