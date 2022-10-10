package com.linkwechat.domain.envelopes.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.annotation.Excel;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 红包发放客户
 */
@Data
public class WeCutomerRedEnvelopesVo {

    @Excel(name = "发放员工")
    private String userName;
    @Excel(name = "领取客户")
    private String customerName;

    @Excel(name = "红包金额（元）")
    private String redEnvelopeMoney;
    @Excel(name = "发放时间",dateFormat="yyyy-MM-dd HH:mm:ss")
     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @Excel(name = "发放状态")
    private Integer sendState;
    @Excel(name = "交易订单号")
    private String orderNo;
    //客户类型
    private Integer customerType;
    //客户头像
    private String avatar;
}
