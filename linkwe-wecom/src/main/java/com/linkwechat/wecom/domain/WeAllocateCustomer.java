package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
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
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeAllocateCustomer
{
    private static final long serialVersionUID = 1L;


    /** 接替成员的userid */
    private String takeoverUserid;

    /** 被分配的客户id */
    private String externalUserid;

    /** 分配时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date allocateTime;

    /** 原跟进成员的userid */
    private String handoverUserid;

    /**
     * 接替状态， 1-接替完毕 2-等待接替 3-客户拒绝 4-接替成员客户达到上限 5-无接替记录
     */
    private Integer status;

    /**
     * 接替客户的时间，如果是等待接替状态，则为未来的自动接替时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date takeoverTime;

    /**
     * 失败原因
     */
    private String failReason;


    /**
     * 0:离职继承;1:在职继承;
     */
    private  Integer extentType;
}
