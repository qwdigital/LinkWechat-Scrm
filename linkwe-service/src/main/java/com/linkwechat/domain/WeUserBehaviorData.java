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
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 联系客户统计数据 (WeUserBehaviorData)
 *
 * @author danmo
 * @since 2022-04-30 23:28:06
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_user_behavior_data")
public class WeUserBehaviorData extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 成员id
     */
    @ApiModelProperty(value = "成员id")
    @TableField("user_id")
    private String userId;


    /**
     * 数据日期，为当日0点的时间戳
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "数据日期，为当日0点的时间戳")
    @TableField("stat_time")
    private Date statTime;


    /**
     * 发起申请数
     */
    @ApiModelProperty(value = "发起申请数")
    @TableField("new_apply_cnt")
    private Integer newApplyCnt;


    /**
     * 新增客户数，成员新添加的客户数量
     */
    @ApiModelProperty(value = "新增客户数，成员新添加的客户数量")
    @TableField("new_contact_cnt")
    private Integer newContactCnt;


    /**
     * 聊天总数， 成员有主动发送过消息的单聊总数
     */
    @ApiModelProperty(value = "聊天总数， 成员有主动发送过消息的单聊总数")
    @TableField("chat_cnt")
    private Integer chatCnt;


    /**
     * 发送消息数，成员在单聊中发送的消息总数
     */
    @ApiModelProperty(value = "发送消息数，成员在单聊中发送的消息总数")
    @TableField("message_cnt")
    private Integer messageCnt;


    /**
     * 已回复聊天占比，浮点型，客户主动发起聊天后，成员在一个自然日内有回复过消息的聊天数/客户主动发起的聊天数比例，不包括群聊，仅在确有聊天时返回
     */
    @ApiModelProperty(value = "已回复聊天占比，浮点型，客户主动发起聊天后，成员在一个自然日内有回复过消息的聊天数/客户主动发起的聊天数比例，不包括群聊，仅在确有聊天时返回")
    @TableField("reply_percentage")
    private String replyPercentage;


    /**
     * 平均首次回复时长
     */
    @ApiModelProperty(value = "平均首次回复时长")
    @TableField("avg_reply_time")
    private Integer avgReplyTime;


    /**
     * 删除/拉黑成员的客户数，即将成员删除或加入黑名单的客户数
     */
    @ApiModelProperty(value = "删除/拉黑成员的客户数，即将成员删除或加入黑名单的客户数")
    @TableField("negative_feedback_cnt")
    private Integer negativeFeedbackCnt;


    
    
    


    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    @TableField("del_flag")
    private Integer delFlag;
}
