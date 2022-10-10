package com.linkwechat.domain.envelopes.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class WeGroupRedEnvelopesVo {
    @Excel(name = "发放员工")
    private String userName;
    @Excel(name = "领取客户群")
    private String groupName;
    @Excel(name = "红包类型")
    private Integer redEnvelopeType;
    @Excel(name = "红包个数")
    private Integer redEnvelopeNum;
    @Excel(name = "红包金额(元)")
    private String redEnvelopeMoney;

    @Excel(name = "发放时间",dateFormat="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String chatId;

    //群主名称
    private String groupLeaderName;

    //领取数量
    private  Integer receiveNum;

    //红包剩余数量
    private Integer surplusNum;

    //订单id
    private String orderNo;
}
