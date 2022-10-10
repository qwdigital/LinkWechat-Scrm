package com.linkwechat.domain.envelopes.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class WeRedEnvelopesCountVo {
    //累计支出金额
    private int  totalMoney;
    //累计支出笔数
    private Integer  totalNum;
    //今日支出金额(元)
    private int currentMoney;
    //今日支出笔数
    private Integer currentNum;

    //员工姓名
    private String userName;

    //红包领取人:1:好友客户;2:群成员
    private Integer receiveType;
    //发送时间
     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
