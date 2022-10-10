package com.linkwechat.domain.wecom.vo.kf;

import lombok.Data;

import java.util.Date;

/**
 * @author danmo
 * @Description 客服事件消息
 * @date 2021/12/13 10:57
 **/
@Data
public class WeKfSyncEventMsgVo {

    /**
     * 企业ID
     */
    private String corpId;
    
    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 消息发送时间
     */
    private Date sendTime;

    /**
     * 消息来源。3-微信客户发送的消息 4-系统推送的事件消息 5-接待人员在企业微信客户端发送的消息
     */
    private Integer origin;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 客服帐号ID
     */
    private String openKfId;

    /**
     * 客户UserID
     */
    private String externalUserId;

    /**
     * 进入会话的场景值
     */
    private String scene;

    /**
     * 进入会话的自定义参数
     */
    private String sceneParam;

    /**
     * 发送欢迎语
     */
    private String welcomeCode;

    /**
     * 发送失败的消息msgid
     */
    private String failMsgId;

    /**
     * 失败类型。0-未知原因 1-客服账号已删除 2-应用已关闭 4-会话已过期，超过48小时 5-会话已关闭 6-超过5条限制 7-未绑定视频号 8-主体未验证 9-未绑定视频号且主体未验证 10-用户拒收
     */
    private Integer failType;

    /**
     * 接待人员userid
     */
    private String servicerUserId;

    /**
     * 状态类型。1-接待中 2-停止接待
     */
    private Integer status;

    /**
     * 变更类型，均为接待人员在企业微信客户端操作触发。1-从接待池接入会话 2-转接会话 3-结束会话 4-重新接入已结束/已转接会话
     */
    private Integer changeType;

    /**
     * 老的接待人员userid。仅change_type为2、3和4有值
     */
    private String oldServicerUserId;

    /**
     * 新的接待人员userid。仅change_type为1、2和4有值
     */
    private String newServicerUserId;

    /**
     * 用于发送事件响应消息的code，仅change_type为1和3时，会返回该字段
     */
    private String msgCode;


}
