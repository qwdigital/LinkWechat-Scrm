package com.linkwechat.domain.live;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class WeLiveTaskUserDetail {

    //员工名称
    private String userName;

    //预计发送人数
    private int estimateSendNumber;

    //实际发送人数
    private int actualSendNumber;

    //客户名称
    private String customerOrGroupName;

    //送达时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date updateTime;


    //发送状态：0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败
    private Integer sendState;


}
