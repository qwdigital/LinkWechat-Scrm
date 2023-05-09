package com.linkwechat.domain.envelopes.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.converter.DateConverter;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 红包发放客户
 */
@Data
public class WeCutomerRedEnvelopesVo {

    @ExcelProperty( "发放员工")
    private String userName;
    @ExcelProperty("领取客户")
    private String customerName;

    @ExcelProperty("红包金额（元）")
    private String redEnvelopeMoney;
    @ExcelProperty(value = "发放时间",converter = DateConverter.class)
     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ExcelProperty("发放状态")
    private Integer sendState;
    @ExcelProperty("交易订单号")
    private String orderNo;

    //客户类型
    @ExcelIgnore
    private Integer customerType;
    //客户头像
    @ExcelIgnore
    private String avatar;
}
