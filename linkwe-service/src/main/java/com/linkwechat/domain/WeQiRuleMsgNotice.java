package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 质检规则通知表(WeQiRuleMsgNotice)
 *
 * @author danmo
 * @since 2023-05-10 09:51:51
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_qi_rule_msg_notice")
public class WeQiRuleMsgNotice extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 质检消息id
     */
    @ApiModelProperty(value = "质检消息id")
    @TableField("qi_rule_msg_id")
    private Long qiRuleMsgId;


    /**
     * 发送人id
     */
    @ApiModelProperty(value = "发送人id")
    @TableField("user_id")
    private String userId;


    /**
     * 类型 1-普通 2-督导
     */
    @ApiModelProperty(value = "类型 1-普通 2-督导")
    @TableField("type")
    private Integer type;


    /**
     * 消息状态：1-待发送 2-已发送 3-发送失败 4-已撤回
     */
    @ApiModelProperty(value = "消息状态：1-待发送 2-已发送 3-发送失败 4-已撤回")
    @TableField("status")
    private Integer status;


    /**
     * 消息ID
     */
    @ApiModelProperty(value = "消息ID")
    @TableField("msg_id")
    private String msgId;


    /**
     * 无效成员ID
     */
    @ApiModelProperty(value = "无效成员ID")
    @TableField("invalid_user")
    private String invalidUser;


    /**
     * 没有基础接口许可(包含已过期)的userid
     */
    @ApiModelProperty(value = "没有基础接口许可(包含已过期)的userid")
    @TableField("unlicensed_user")
    private String unlicensedUser;


    /**
     * 更新模版卡片消息CODE
     */
    @ApiModelProperty(value = "更新模版卡片消息CODE")
    @TableField("response_code")
    private String responseCode;

    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    @TableField("del_flag")
    private Integer delFlag;

}
