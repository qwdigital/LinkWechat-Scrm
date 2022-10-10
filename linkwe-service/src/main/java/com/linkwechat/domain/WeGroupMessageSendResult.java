package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 群发消息成员执行结果表(WeGroupMessageSendResult)
 *
 * @author danmo
 * @since 2022-04-06 22:29:05
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_group_message_send_result")
public class WeGroupMessageSendResult extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 消息模板id
     */
    @ApiModelProperty(value = "消息模板id")
    @TableField("msg_template_id")
    private Long msgTemplateId;


    /**
     * 企业群发消息的id
     */
    @ApiModelProperty(value = "企业群发消息的id")
    @TableField("msg_id")
    private String msgId;


    /**
     * 企业服务人员的userid
     */
    @ApiModelProperty(value = "企业服务人员的userid")
    @TableField("user_id")
    private String userId;


    /**
     * 外部联系人userid
     */
    @ApiModelProperty(value = "外部联系人userid")
    @TableField("external_userid")
    private String externalUserid;


    /**
     * 外部客户群id
     */
    @ApiModelProperty(value = "外部客户群id")
    @TableField("chat_id")
    private String chatId;


    /**
     * 发送状态：0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败
     */
    @ApiModelProperty(value = "发送状态：0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败")
    @TableField("status")
    private Integer status;


    /**
     * 发送时间，发送状态为1时返回
     */
    @ApiModelProperty(value = "发送时间，发送状态为1时返回")
    @TableField("send_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date sendTime;


    
    
    


    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    @TableField("del_flag")
    private Integer delFlag;

    /**
     * 企业服务人员名称
     */
    @ApiModelProperty("企业服务人员名称")
    @TableField(exist = false)
    private String userName;

    /**
     * 外部联系人姓名
     */
    @ApiModelProperty("外部联系人姓名")
    @TableField(exist = false)
    private String customerName;

    /**
     * 外部客户群名称
     */
    @ApiModelProperty("外部客户群名称")
    @TableField(exist = false)
    private String chatName;

    @TableField(exist = false)
    private List<Long> msgTemplateIds;
}
