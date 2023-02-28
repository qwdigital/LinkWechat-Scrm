package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 客服客户表(WeKfCustomer)
 *
 * @author danmo
 * @since 2022-04-15 15:53:34
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_kf_customer")
public class WeKfCustomer extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业id")
    @TableField("corp_id")
    private String corpId;


    /**
     * 微信客户的external_userid
     */
    @ApiModelProperty(value = "微信客户的external_userid")
    @TableField("external_userid")
    private String externalUserid;


    /**
     * 微信昵称
     */
    @ApiModelProperty(value = "微信昵称")
    @TableField("nick_name")
    private String nickName;


    /**
     * 微信头像
     */
    @ApiModelProperty(value = "微信头像")
    @TableField("avatar")
    private String avatar;


    /**
     * unionid
     */
    @ApiModelProperty(value = "unionid")
    @TableField("union_id")
    private String unionId;


    /**
     * 性别 0-未知 1-男性 2-女性
     */
    @ApiModelProperty(value = "性别 0-未知 1-男性 2-女性")
    @TableField("gender")
    private Integer gender;


    /**
     * 是否删除:0有效,1删除
     */
    @ApiModelProperty(value = "是否删除:0有效,1删除")
    @TableField("del_flag")
    private Integer delFlag;
}
