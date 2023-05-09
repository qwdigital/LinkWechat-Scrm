package com.linkwechat.domain.envelopes.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.converter.DateConverter;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class WeGroupRedEnvelopesVo {
    @ExcelProperty("发放员工")
    private String userName;
    @ExcelProperty("领取客户群")
    private String groupName;
    @ExcelProperty("红包类型")
    private Integer redEnvelopeType;
    @ExcelProperty("红包个数")
    private Integer redEnvelopeNum;
    @ExcelProperty("红包金额(元)")
    private String redEnvelopeMoney;

    @ExcelProperty(value = "发放时间",converter = DateConverter.class)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ExcelIgnore
    private String chatId;

    //群主名称
    @ExcelIgnore
    private String groupLeaderName;

    //领取数量
    @ExcelIgnore
    private  Integer receiveNum;

    //红包剩余数量
    @ExcelIgnore
    private Integer surplusNum;

    //订单id
    @ExcelIgnore
    private String orderNo;
}
