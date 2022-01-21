package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * 企业微信客户对象 we_customer
 *
 * @author ruoyi
 * @date 2020-09-13
 */
@ApiModel
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_customer")
public class WeCustomer extends BaseEntity {
    private static final long serialVersionUID = 1L;


    //客户id
    private String externalUserid;


    //客户名称
    @TableField(value = "name")
    private String customerName;


    //客户类型
    private Integer customerType;


    //头像
    private String avatar;


    //0-未知 1-男性 2-女性
    private Integer gender;


    private String unionid;


    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;


    private String corpName;


    private String position;


    private Integer isOpenChat;


    private String firstUserId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date firstAddTime;


    private Integer trackState;


    private String trackContent;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date trackTime;



    private String phone;

    private String email;

    private String qq;

    private String otherDescr;


    private String address;

    private Integer addMethod;


    private Integer delFlag;


    private String state;

    private String takeoverUserId;

    private String tagIds;












}
