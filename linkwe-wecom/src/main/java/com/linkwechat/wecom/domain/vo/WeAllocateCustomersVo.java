package com.linkwechat.wecom.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @description: 已分配客户信息
 * @author: HaoN
 * @create: 2020-10-28 00:13
 **/
@Data
public class WeAllocateCustomersVo extends BaseEntity {
    /**客户名称*/
    private String customerName;

    /**接替人名称*/
    private String takeUserName;

    /**分配时间*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date allocateTime;

    /**接替人所在部门*/
    private String department;

    /**原拥有着*/
    private String handoverUserId;






    private String avatar;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;


    //0-未知 1-男性 2-女性
    private Integer gender;


    //查询标签id
    private String tagIds;

    //标签名称，使用逗号隔开
    private String tagNames;

    //跟进人名称
    private String userName;


    //添加时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date firstAddTime;


}
