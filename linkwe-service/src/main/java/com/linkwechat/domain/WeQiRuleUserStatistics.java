package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 会话质检员工数据统计表(WeQiRuleUserStatistics)
 *
 * @author danmo
 * @since 2023-05-17 13:50:44
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_qi_rule_user_statistics")
public class WeQiRuleUserStatistics extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 员工ID
     */
    @ApiModelProperty(value = "员工ID")
    @TableField("we_user_id")
    private String weUserId;


    /**
     * 客户会话数
     */
    @ApiModelProperty(value = "客户会话数")
    @TableField("chat_num")
    private String chatNum;


    /**
     * 客群会话数
     */
    @ApiModelProperty(value = "客群会话数")
    @TableField("group_chat_num")
    private String groupChatNum;


    /**
     * 成员回复次数
     */
    @ApiModelProperty(value = "成员回复次数")
    @TableField("reply_num")
    private String replyNum;


    /**
     * 成员超时次数
     */
    @ApiModelProperty(value = "成员超时次数")
    @TableField("time_out_num")
    private String timeOutNum;


    /**
     * 成员超时率
     */
    @ApiModelProperty(value = "成员超时率")
    @TableField("time_out_rate")
    private String timeOutRate;


    /**
     * 客户会话超时率
     */
    @ApiModelProperty(value = "客户会话超时率")
    @TableField("chat_time_out_rate")
    private String chatTimeOutRate;


    /**
     * 客群会话超时率
     */
    @ApiModelProperty(value = "客群会话超时率")
    @TableField("group_chat_time_out_rate")
    private String groupChatTimeOutRate;

    /**
     * 统计时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "统计时间")
    @TableField("state_time")
    private Date stateTime;

    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    @TableField("del_flag")
    private Integer delFlag;

}
