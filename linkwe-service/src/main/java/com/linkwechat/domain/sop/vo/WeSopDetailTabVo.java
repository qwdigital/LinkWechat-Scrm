package com.linkwechat.domain.sop.vo;

import lombok.Data;

@Data
public class WeSopDetailTabVo {
    //今日客户或客群数
    private int tdCustomerNumber;
    //今日正常结束客户或客群数
    private int tdCommonEndCustomerNumber;
    //今日提前结束客户或客群数
    private int tdEarlyEndCustomerNumber;
    //今日异常结束客户或客群数
    private int tdErrorEndCustomerNumber;
    //昨日客户或客群数
    private int ydCustomerNumber;
    //昨日正常结束客户或客群数
    private int ydCommonEndCustomerNumber;
    //昨日提前结束客户或客群数
    private int ydEarlyEndCustomerNumber;
    //昨日异常结束客户或客群数
    private int ydErrorEndCustomerNumber;
}
