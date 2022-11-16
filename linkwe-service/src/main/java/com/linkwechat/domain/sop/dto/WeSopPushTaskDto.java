package com.linkwechat.domain.sop.dto;

import lombok.Data;

import java.util.Date;

/**
 * 推送任务相关
 */
@Data
public class WeSopPushTaskDto {
    //sop发送类型(1:企业微信发送;2:手动发送)
    private Integer sendType;

    //执行员工id
    private String executeWeUserId;

    //目标id
    private String targetId;

    //素材id
    private String sopAttachmentId;

    //目标类型1:客户 2:群
    private Integer targetType;

    //sop主键
    private String sopBaseId;

    //开始时间
    private Date pushStartTime;

    //结束时间
    private Date pushEndTime;

    //具体执行任务的id
    private String excuteTargetAttachId;

}
