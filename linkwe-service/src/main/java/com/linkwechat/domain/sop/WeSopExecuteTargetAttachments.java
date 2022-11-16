package com.linkwechat.domain.sop;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 目标执行内容
 * @TableName we_sop_execute_target_attachments
 */
@TableName(value ="we_sop_execute_target_attachments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeSopExecuteTargetAttachments extends BaseEntity{
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 目标执行对象主键
     */
    private Long executeTargetId;

    /**
     * 执行内容的主键
     */
    private Long sopAttachmentId;


    /**
     *推送时间类型(1:特定时间推送，比如2022-08-21推送日期;
     * 2:周期推送，数字字符串型1-7，对应周一到周日;3:相对推送时间,数字字符串型，比如2022-08-21添加的客户，那么相对这个时间第一天推送，则值为1，但是对应的实际推送时间为，2022-08-22) 注:此处只供前端做展示
     */
    private Integer pushTimeType;

    /**
     * 推送时间前缀，分为数字型跟日期格式行字符串 注:前端做展示
     */
    private String pushTimePre;

    /**
     * 推送具体开始时间
     */
    private Date pushStartTime;


    /**
     * 是否发送:0:未发送推送发送;1:已发送推送
     */
    private Integer isTip;

    /**
     * 推送具体结束时间
     */
    private Date pushEndTime;


    /**
     * 执行完成时间
     */
    private Date executeTime;

    /**
     * 执行状态(0:未执行;1:已执行)
     */
    private Integer executeState;


    /**
     * 是否准时推送(0:准时推送;1:迟到推送;)
     */
    private Integer isPushOnTime;


    /**
     * sop发送类型(1:企业微信发送;2:手动发送)
     */
    private Integer sendType;


    /**
     * 企业群发消息的id，可用于获取群发消息发送结果,手动发送方式没有
     */
    private String msgId;


    /**
     * 删除标识 0 有效 1删除
     */
    private Integer delFlag;

}