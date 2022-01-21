package com.linkwechat.wecom.domain;


import com.baomidou.mybatisplus.annotation.*;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 活码信息表(WeQrCode)$desc
 *
 * @author danmo
 * @since 2021-11-07 02:24:34
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_qr_code")
public class WeQrCode extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 活码名称
     */
    @ApiModelProperty(value = "活码名称")
    @TableField("name")
    private String name;


    /**
     * 活码分组id
     */
    @ApiModelProperty(value = "活码分组id")
    @TableField("group_id")
    private Long groupId;


    /**
     * 添加是否无需验证 0：否 1：是
     */
    @ApiModelProperty(value = "添加是否无需验证 0：否 1：是")
    @TableField("auto_add")
    private Integer autoAdd;


    /**
     * 活码类型 1：单人 2：多人
     */
    @ApiModelProperty(value = "活码类型 1：单人 2：多人")
    @TableField("type")
    private Integer type;


    /**
     * 排期类型 1：全天 2：自定义
     */
    @ApiModelProperty(value = "排期类型 1：全天 2：自定义")
    @TableField("rule_type")
    private Integer ruleType;


    /**
     * 添加渠道
     */
    @ApiModelProperty(value = "添加渠道")
    @TableField("state")
    private String state;


    /**
     * 二维码配置id
     */
    @ApiModelProperty(value = "二维码配置id")
    @TableField("config_id")
    private String configId;

    /**
     * 二维码地址
     */
    @ApiModelProperty(value = "二维码地址")
    @TableField("qr_code")
    private String qrCode;

    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除",hidden = true)
    @TableField("del_flag")
    private Integer delFlag;
}
