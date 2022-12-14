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
import java.util.Date;

/**
 * 客服事件消息表(WeKfEventMsg)
 *
 * @author danmo
 * @since 2022-04-15 15:53:35
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_kf_event_msg")
public class WeKfEventMsg extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业id")
    @TableField("corp_id")
    private String corpId;


    /**
     * 消息id
     */
    @ApiModelProperty(value = "消息id")
    @TableField("msg_id")
    private String msgId;


    /**
     * 客服帐号ID
     */
    @ApiModelProperty(value = "客服帐号ID")
    @TableField("open_kf_id")
    private String openKfId;


    /**
     * 消息发送时间
     */
    @ApiModelProperty(value = "消息发送时间")
    @TableField("send_time")
    private Date sendTime;


    /**
     * 消息来源。3-微信客户发送的消息 4-系统推送的事件消息 5-接待人员在企业微信客户端发送的消息
     */
    @ApiModelProperty(value = "消息来源。3-微信客户发送的消息 4-系统推送的事件消息 5-接待人员在企业微信客户端发送的消息")
    @TableField("origin")
    private Integer origin;


    /**
     * 事件类型
     */
    @ApiModelProperty(value = "事件类型")
    @TableField("event_type")
    private String eventType;


    /**
     * 客户UserID
     */
    @ApiModelProperty(value = "客户UserID")
    @TableField("external_userid")
    private String externalUserid;


    /**
     * 接待人员userid
     */
    @ApiModelProperty(value = "接待人员userid")
    @TableField("servicer_userid")
    private String servicerUserid;


    /**
     * 老的接待人员userid
     */
    @ApiModelProperty(value = "老的接待人员userid")
    @TableField("old_servicer_userid")
    private String oldServicerUserid;


    /**
     * 新的接待人员userid
     */
    @ApiModelProperty(value = "新的接待人员userid")
    @TableField("new_servicer_userid")
    private String newServicerUserid;


    /**
     * 变更类型。1-从接待池接入会话 2-转接会话 3-结束会话 4-重新接入已结束/已转接会话
     */
    @ApiModelProperty(value = "变更类型。1-从接待池接入会话 2-转接会话 3-结束会话 4-重新接入已结束/已转接会话")
    @TableField("change_type")
    private Integer changeType;


    /**
     * 状态类型。1-接待中 2-停止接待
     */
    @ApiModelProperty(value = "状态类型。1-接待中 2-停止接待")
    @TableField("status")
    private Integer status;


    /**
     * 进入会话的场景值
     */
    @ApiModelProperty(value = "进入会话的场景值")
    @TableField("scene")
    private String scene;


    /**
     * 进入会话的自定义参数
     */
    @ApiModelProperty(value = "进入会话的自定义参数")
    @TableField("scene_param")
    private String sceneParam;


    /**
     * 用于发送事件响应消息的code
     */
    @ApiModelProperty(value = "用于发送事件响应消息的code")
    @TableField("msg_code")
    private String msgCode;


    /**
     * 欢迎语code
     */
    @ApiModelProperty(value = "欢迎语code")
    @TableField("welcome_code")
    private String welcomeCode;


    /**
     * 发送失败的消息msgid
     */
    @ApiModelProperty(value = "发送失败的消息msgid")
    @TableField("fail_msg_id")
    private String failMsgId;


    /**
     * 失败类型:0-未知原因 1-客服账号已删除 2-应用已关闭 4-会话已过期，超过48小时 5-会话已关闭 6-超过5条限制 7-未绑定视频号 8-主体未验证 9-未绑定视频号且主体未验证 10-用户拒收
     */
    @ApiModelProperty(value = "失败类型:0-未知原因 1-客服账号已删除 2-应用已关闭 4-会话已过期，超过48小时 5-会话已关闭 6-超过5条限制 7-未绑定视频号 8-主体未验证 9-未绑定视频号且主体未验证 10-用户拒收")
    @TableField("fail_type")
    private Integer failType;


    /**
     * 是否删除:0有效,1删除
     */
    @ApiModelProperty(value = "是否删除:0有效,1删除")
    @TableField("del_flag")
    private Integer delFlag;
}
