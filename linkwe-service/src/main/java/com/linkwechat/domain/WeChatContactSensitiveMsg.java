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
 * 会话触发敏感词记录(WeChatContactSensitiveMsg)
 *
 * @author danmo
 * @since 2022-06-10 10:38:47
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_chat_contact_sensitive_msg")
public class WeChatContactSensitiveMsg extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 消息id
     */
    @ApiModelProperty(value = "消息id")
    @TableField("msg_id")
    private String msgId;


    /**
     * 通知发送状态
     */
    @ApiModelProperty(value = "通知发送状态")
    @TableField("send_status")
    private Integer sendStatus;


    /**
     * 匹配词
     */
    @ApiModelProperty(value = "匹配词")
    @TableField("pattern_words")
    private String patternWords;


    /**
     * 匹配内容
     */
    @ApiModelProperty(value = "匹配内容")
    @TableField("content")
    private String content;


    /**
     * 发送人id
     */
    @ApiModelProperty(value = "发送人id")
    @TableField("from_id")
    private String fromId;


    /**
     * 发送时间
     */
    @ApiModelProperty(value = "发送时间")
    @TableField("msg_time")
    private Date msgTime;

    /**
     * 删除标识 0 正常 1 删除
     */
    @ApiModelProperty(value = "删除标识 0 正常 1 删除")
    @TableField("del_flag")
    private Integer delFlag;
}
