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
 * 微信用户表(WxUser)
 *
 * @author danmo
 * @since 2022-07-01 13:42:38
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("wx_user")
public class WxUser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1


    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    /**
     * 微信用户ID
     */
    @ApiModelProperty(value = "微信用户ID")
    @TableField("open_id")
    private String openId;


    /**
     * 开放平台用户ID
     */
    @ApiModelProperty(value = "开放平台用户ID")
    @TableField("union_id")
    private String unionId;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    @TableField("nick_name")
    private String nickName;


    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    @TableField("avatar")
    private String avatar;


    /**
     * 性别 0-未知 1-男性 2-女性
     */
    @ApiModelProperty(value = "性别 0-未知 1-男性 2-女性")
    @TableField("sex")
    private Integer sex;


    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    @TableField("phone")
    private String phone;


    /**
     * 用户个人资料填写的省份
     */
    @ApiModelProperty(value = "用户个人资料填写的省份")
    @TableField("province")
    private String province;


    /**
     * 普通用户个人资料填写的城市
     */
    @ApiModelProperty(value = "普通用户个人资料填写的城市")
    @TableField("city")
    private String city;


    /**
     * 国家，如中国为CN
     */
    @ApiModelProperty(value = "国家，如中国为CN")
    @TableField("country")
    private String country;


    /**
     * 用户特权信息，json 数组
     */
    @ApiModelProperty(value = "用户特权信息，json 数组")
    @TableField("privilege")
    private String privilege;


    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）")
    @TableField("del_flag")
    private Integer delFlag;


    /**
     * 是否开启文件预览 true:开启 false:不开启
     */
    @TableField(exist = false)
    private boolean enableFilePreview;

}
