package com.linkwechat.domain.sop.vo.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 今日待发送相关信息
 */
@Data
public class WeSopToBeSentContentInfoVo {
   //sop主键
    private String sopBaseId;
    //sop名称
    private String sopName;
    //sop开始发送
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pushStartTime;
    //sop结束发送时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pushEndTime;
}
