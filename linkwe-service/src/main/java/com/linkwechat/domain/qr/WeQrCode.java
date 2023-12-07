package com.linkwechat.domain.qr;


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
     * 排班方式 1：轮询 2：顺序 3：随机
     */
    @ApiModelProperty("排班方式 1：轮询 2：顺序 3：随机")
    @TableField("rule_mode")
    private Integer ruleMode;

    /**
     * 开启备用员工 0：否 1：是
     */
    @ApiModelProperty("开启备用员工 0：否 1：是")
    @TableField("open_spare_user")
    private Integer openSpareUser = 0;

    /**
     * 是否开启同一外部企业客户只能添加同一个员工，开启后，同一个企业的客户会优先添加到同一个跟进人  0-不开启 1-开启
     */
    @ApiModelProperty("是否开启同一外部企业客户只能添加同一个员工，开启后，同一个企业的客户会优先添加到同一个跟进人  0-不开启 1-开启")
    @TableField("is_exclusive")
    private Integer isExclusive = 0;

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

    /**
     * 欢迎语开关
     */
    @ApiModelProperty("欢迎语开关 1-不发送欢迎语，2-发送欢迎语")
    @TableField("qr_welcome_open")
    private Integer qrWelcomeOpen;

    /**
     * 是否优先员工欢迎语
     */
    @ApiModelProperty("是否优先员工欢迎语 0-否，1-是（仅欢迎语开关为2是生效）")
    @TableField("qr_priority_user_welcome")
    private Integer qrPriorityUserWelcome;
}
