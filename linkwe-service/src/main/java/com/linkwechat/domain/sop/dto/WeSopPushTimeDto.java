package com.linkwechat.domain.sop.dto;

import lombok.Data;

@Data
public class WeSopPushTimeDto {

    private Long sopPushTimeId;
    private String pushStartTime;
    private String pushEndTime;
    private String pushTimePre;
    private Integer pushTimeType;
    private Long sopAttachmentId;

}
