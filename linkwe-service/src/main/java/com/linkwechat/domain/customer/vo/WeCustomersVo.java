package com.linkwechat.domain.customer.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeCustomersVo {

    private Long id;

    //头像
    private String avatar;

    //客户名称
    private String customerName;

    //客户类型 1:微信用户，2:企业用户
    private Integer customerType;

    //客户id
    private String externalUserid;

    //跟进人id
    private String firstUserId;

    //跟踪状态 1:待跟进;2:跟进中;3:已成交;4:无意向;5:已流失
    private Integer trackState;

    //添加方式
    private Integer addMethod;

    //添加时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date firstAddTime;


    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    //留存天数
    private Integer retentionDays;

    //0-未知 1-男性 2-女性
    private Integer gender;

    //手机号
    private String phone;

    //邮箱
    private String email;

    //地址
    private String address;

    //qq
    private String qq;

    //职位
    private String position;

    //公司名称
    private String corpName;

    //描述
    private String otherDescr;

    //跟进人名称
    private String userName;

    //个人标签id
    private String personTagIds;

    //个人标签名
    private String personTagNames;


    //查询标签id(企业标签id)
    private String tagIds;

    //标签名称，使用逗号隔开(企业标签名)
    private String tagNames;


    //跟进时间或流失时间
    private Date trackTime;

    //数据更新时间,也可表示客户流失时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;




}
