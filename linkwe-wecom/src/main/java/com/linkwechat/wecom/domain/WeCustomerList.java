package com.linkwechat.wecom.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeCustomerList {


    //头像
    private String avatar;
    //客户名称
    private String customerName;

    //客户类型 1:微信用户，2:企业用户
    private Integer customerType;

    //查询标签id(企业标签id)
    private String tagIds;

    //标签名称，使用逗号隔开(企业标签名)
    private String tagNames;

    //个人标签id
    private String personTagIds;

    //个人标签名
    private String personTagNames;

    //跟进人名称
    private String userName;

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

    //是否可以被继承 1:是;0:否
    private Boolean isTransfer;


    //0-未知 1-男性 2-女性
    private Integer gender;


    //所属部门
    private String departmentName;









    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;


//    private List<WeFlowerCustomerRel> weFlowerCustomerRels;

    //查询条件客户姓名
    private String name;
    //查询条件
    private String userIds;
    //查询开始时间
    private String beginTime;
    //查询结束时间
    private String endTime;

    //所属部门id，多个用逗号隔开
    private String departmentIds;


    private String remark;

    private String phone;

    private String email;

    private String address;

    private String qq;

    private String position;

    private String corpName;

    private String otherDescr;


    //0正常；1:删除;
    private Integer delFlag;
}
