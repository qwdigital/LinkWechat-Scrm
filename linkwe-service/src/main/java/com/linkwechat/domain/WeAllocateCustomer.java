package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 离职分配的客户列对象 we_allocate_customer
 *
 * @author ruoyi
 * @date 2020-10-24
 */
@Data
@TableName("we_allocate_customer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeAllocateCustomer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    /** 接替成员的userid */
    private String takeoverUserid;

    /**离职成员主键*/
    private Long leaveUserId;

    /** 被分配的客户id */
    private String externalUserid;


    /**客户名称*/
    private String customerName;

    /**接替成员名称*/
    private String takeoverName;

    /**接替成员部门名称*/
    private String takeoverDeptName;

    /** 分配时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date allocateTime;

    /** 原跟进成员的userid */
    private String handoverUserid;

    /**
     * 接替状态， 1:等待接替 2:接替中(等待微信接替) 3:接替成功 4:接替失败
     */
    private Integer status;

    /**
     * 接替客户的时间，如果是等待接替状态，则为未来的自动接替时间
     */
     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date takeoverTime;

    /**
     * 失败原因
     */
    private String failReason;

    @TableField(exist = false)
    private String takeUserName;

//    @TableField(exist = false)
//    private String customerName;

    @TableField(exist = false)
    private String deptNames;

    @TableField(exist = false)
    private String firstUserId;


    /**
     * 0:离职继承;1:在职继承;
     */
    private  Integer extentType;
}

