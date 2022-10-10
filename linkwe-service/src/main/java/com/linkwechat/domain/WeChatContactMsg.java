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
 * 会话消息(WeChatContactMsg)
 *
 * @author danmo
 * @since 2022-05-06 11:54:51
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_chat_contact_msg")
public class WeChatContactMsg extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
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
     * 发送人id
     */
    @ApiModelProperty(value = "发送人id")
    @TableField("from_id")
    private String fromId;


    /**
     * 接收人id（列表）
     */
    @ApiModelProperty(value = "接收人id（列表）")
    @TableField("to_list")
    private String toList;


    /**
     * 群聊id
     */
    @ApiModelProperty(value = "群聊id")
    @TableField("room_id")
    private String roomId;


    /**
     * 消息类型
     */
    @ApiModelProperty(value = "消息类型")
    @TableField("action")
    private String action;


    /**
     * 消息类型(如：文本，图片)
     */
    @ApiModelProperty(value = "消息类型(如：文本，图片)")
    @TableField("msg_type")
    private String msgType;


    /**
     * 发送时间
     */
    @ApiModelProperty(value = "发送时间")
    @TableField("msg_time")
    private Date msgTime;


    /**
     * 消息标识
     */
    @ApiModelProperty(value = "消息标识")
    @TableField("seq")
    private Long seq;


    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容")
    @TableField("contact")
    private String contact;


    /**
     * 是否为外部聊天 0 外部 1 内部
     */
    @ApiModelProperty(value = "是否为外部聊天 0 外部 1 内部")
    @TableField("is_external")
    private Integer isExternal;

    /**
     * 删除标识 0正常 1 删除
     */
    @ApiModelProperty(value = "删除标识 0正常 1 删除")
    @TableField("del_flg")
    private Integer delFlg;

    @ApiModelProperty("成员名称")
    @TableField(exist = false)
    private String userName;

    @ApiModelProperty("客户名称")
    @TableField(exist = false)
    private String customerName;
}
