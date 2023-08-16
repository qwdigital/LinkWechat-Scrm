package com.linkwechat.domain.customer.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.linkwechat.domain.WeCustomerInfoExpand;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * @description: 客户画像实体VO
 * @author: HaoN
 * @create: 2021-03-03 11:49
 **/
@Data
public class WeCustomerPortraitVo {

    private Long customerId;
    //外部联系人id
    private String externalUserid;
    //企业员工id
    private String userId;
    //客户与企业员工关系id
    private String flowerCustomerRelId;
    //客户昵称
    private String name;
    //客户性别(0-未知 1-男性 2-女性)
    private Integer gender;
    //客户备注
    private String remark;
    //备注客户手机号
    private String remarkMobiles;
    //客户生日
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    //邮箱
    private String email;
    //地址
    private String address;
    //qq
    private String qq;
    //职业
    private String position;
    //公司
    private String remarkCorpName;
    //描述
    private String otherDescr;
    //年纪
    private int age;
    //客户头像
    private String avatar;


    //客户社交关系
    private WeCustomerSocialConnVo socialConn;

    //企业标签名,多个逗号隔开
    private String tagNames;

    //企业标签id,多个逗号隔开
    private String tagIds;


    //个人标签名
    private String personTagNames;

    //个人标签id
    private String personTagIds;

    //当前跟进状态:1:待跟进;2:跟进中;3:已成交;4:无意向;5:已流失
    private Integer trackState;

    //客户类型 1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户
    private Integer customerType;

    //跟进动态
    private String trackContent;

    //省id
    private Integer provinceId;

    //市id
    private Integer cityId;

    //区id
    private Integer areaId;

    //省/市/区
    private String area;

    //客户姓名(客户画像做备注)
    private String customerFullName;


    //客户信息拓展
    private List<WeCustomerInfoExpand> weCustomerInfoExpands;

    /**
     * 添加方式
     *
     * @see com.linkwechat.common.enums.CustomerAddWay
     */
    private Integer addMethod;

    /**
     * 添加方式
     */
    private String addMethodStr;

    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-8")
    private Date addTime;


}
