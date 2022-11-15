package com.linkwechat.domain.sop.vo.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


@Data
public class WeGroupSopBaseContentVo {
    //推送时间类型(1:特定时间推送，比如2022-08-21推送日期;
    //2:周期推送，数字字符串型1-7，对应周一到周日;3:相对推送时间,数字字符串型，比如2022-08-21添加的客户，那么相对这个时间第一天推送，则值为1，但是对应的实际推送时间为，2022-08-22) 注:此处只供前端做展示
    private Integer pushTimeType;
    //推送时间前缀，分为数字型跟日期格式行字符串 注:前端做展示
    private String pushTimePre;
    //推送具体开始时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pushStartTime;
    //推送具体结束时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pushEndTime;
    //执行状态(0:未执行;1:已执行)
    private Integer executeState;
    //实际推送时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date executeTime;
    //执行内容的主键
    private String sopAttachmentId;
    //群名
    private String groupName;
    //群id
    private  String chatId;
    //群创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date addTime;
    //sop主键/移动端科技根据当前字段对其进行分组从而实现每个sop下面素材展示
    private String sopBaseId;

    private String sopName;

    //发送天数
    private int sendDayNumber;

    //已发送次数
    private int sendNumber;

    private String executeTargetAttachId;
}
