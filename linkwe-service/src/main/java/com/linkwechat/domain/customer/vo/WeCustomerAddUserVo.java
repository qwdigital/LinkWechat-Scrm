package com.linkwechat.domain.customer.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @description: 客户添加人
 * @author: HaoN
 * @create: 2021-03-08 15:47
 **/
@Data
public class WeCustomerAddUserVo {
    //员工名称
    private String userName;
    //员工id
    private String userId;
    //员工头像
    private String headImageUrl;
    //创建时间
    private Date createTime;

    //添加方式
    private Integer addMethod;

    //添加时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date firstAddTime;

    //跟进状态
    private Integer trackState;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date trackTime;
}
