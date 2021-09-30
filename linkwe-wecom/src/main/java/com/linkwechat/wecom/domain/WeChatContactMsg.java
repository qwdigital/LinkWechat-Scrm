package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 会话消息对象 we_chat_contact_msg
 * 
 * @author ruoyi
 * @date 2021-07-28
 */
@ApiModel
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("we_chat_contact_msg")
public class WeChatContactMsg extends BaseEntity implements Serializable {

private static final long serialVersionUID=1L;


    /** 主键id */
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty("主键id")
    private Long id;

    /** 消息id */
    @Excel(name = "消息id")
    @ApiModelProperty("消息id")
    private String msgId;

    /** 消息记录seq值 */
    @ApiModelProperty(value = "消息记录seq值",hidden = true)
    private Long seq;

    /** 发送人id */
    @Excel(name = "发送人id")
    @ApiModelProperty("发送人id")
    private String fromId;

    /** 接收人id（列表） */
    @Excel(name = "接收人id" , readConverterExp = "列=表")
    @ApiModelProperty("接收人id")
    private String toList;

    /** 群聊id */
    @Excel(name = "群聊id")
    @ApiModelProperty("群聊id")
    private String roomId;

    /** 消息类型 */
    @Excel(name = "消息类型")
    @ApiModelProperty("消息类型")
    private String action;

    /** 消息类型(如：文本，图片) */
    @Excel(name = "消息类型(如：文本，图片)")
    @ApiModelProperty("消息类型(如：文本，图片)")
    private String msgType;

    /** 发送时间 */
    @Excel(name = "发送时间")
    @ApiModelProperty("发送时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date msgTime;

    /** 消息内容 */
    @Excel(name = "消息内容")
    @ApiModelProperty("消息内容")
    private String contact;

    /** 是否为外部聊天 0 外部 1 内部 */
    @ApiModelProperty(value = "是否为外部聊天 0 外部 1 内部", hidden = true)
    private Integer isExternal;

    @ApiModelProperty("成员名称")
    @TableField(exist = false)
    private String userName;

    @ApiModelProperty("客户名称")
    @TableField(exist = false)
    private String customerName;
}
