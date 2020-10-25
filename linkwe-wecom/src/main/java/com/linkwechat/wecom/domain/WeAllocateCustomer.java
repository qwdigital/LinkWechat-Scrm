package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class WeAllocateCustomer
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 接替成员的userid */
    private String takeoverUserid;

    /** 被分配的客户id */
    private String externalUserid;

    /** 分配时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date allocateTime;

    /** 原跟进成员的userid */
    private String handoverUserid;


}
