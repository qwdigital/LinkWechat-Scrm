package com.linkwechat.wecom.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

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



    /**
     * 外部联系人的userid
     */
    @NotBlank(message = "外部联系人的id不可为空")
    @ApiModelProperty("外部联系人的userid")
    private String externalUserid;


    /**
     * 首位添加人
     */
    private String firstUserId;


    /**
     * 外部联系人名称
     */
    @Excel(name = "客户")
    @ApiModelProperty("外部联系人名称")
    private String name;

    /**
     * 外部联系人头像
     */
    @ApiModelProperty("外部联系人头像")
    private String avatar;

    /**
     * 外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户
     */
    @ApiModelProperty("外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户")
    @Excel(name = "客户类型", readConverterExp = "1=微信,2=企业微信")
    private Integer type;

    /**
     * 外部联系人性别 0-未知 1-男性 2-女性
     */
    @ApiModelProperty("外部联系人性别 0-未知 1-男性 2-女性")
    @Excel(name = "性别", readConverterExp = "0=未知,1=男性,2=女性")
    private Integer gender;

    /**
     * 外部联系人在微信开放平台的唯一身份标识,通过此字段企业可将外部联系人与公众号/小程序用户关联起来。
     */
    @ApiModelProperty("外部联系人在微信开放平台的唯一身份标识,通过此字段企业可将外部联系人与公众号/小程序用户关联起来")
    private String unionid;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("生日")
    private Date birthday;


    /**
     * 客户企业简称
     */
    @ApiModelProperty("客户企业简称")
    @Excel(name = "公司名称")
    private String corpName;

    /**
     * 客户企业全称
     */
    @ApiModelProperty("客户企业全称")
    private String corpFullName;

    /**
     * 职位
     */
    @ApiModelProperty("职位")
    private String position;

    /**
     * 是否开启会话存档 0：关闭 1：开启
     */
    @ApiModelProperty("是否开启会话存档 0：关闭 1：开启")
    private Integer isOpenChat;

    /**
     * 添加人员
     */
    @TableField(exist = false)
    private List<WeFlowerCustomerRel> weFlowerCustomerRels;

    /**
     * 添加人id
     */
    @TableField(exist = false)
    private String userIds;

    /**
     * 标签
     */
    @TableField(exist = false)
    private String tagIds;

    @TableField(exist = false)
    @JSONField(defaultValue = "0")
    private Integer status;

    /**
     * 部门
     */
    @TableField(exist = false)
    private String departmentIds;

    /**
     * 添加人名称
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 员工id
     */
    @TableField(exist = false)
    private String userId;


    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String createBy;

    /**
     * 更新者
     */
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String updateBy;


    /** 企业自定义的state参数，用于区分客户具体是通过哪个「联系我」添加，由企业通过创建「联系我」方式指定 */
    private String state;





    /**
     * 首位添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date firstAddTime;



    //qq
    private String qq;

    //邮箱
    private String email;
    //地址
    private String address;

    //手机号
    private String phone;


    /**
     * 0:正常;1:删除
     */
//    @TableLogic
    private Integer delFlag;

    //添加方式
    private Integer addMethod;

}
