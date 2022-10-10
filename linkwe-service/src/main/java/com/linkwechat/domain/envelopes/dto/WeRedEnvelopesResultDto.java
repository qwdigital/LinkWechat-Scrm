package com.linkwechat.domain.envelopes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 发送红包返回结果
 */
@Data
@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeRedEnvelopesResultDto {
    //返回状态码	 SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
    @XmlElement(name = "return_code")
    private String returnCode;
    //返回信息，如非空，为错误原因
    @XmlElement(name = "return_msg")
    private String returnMsg;
    //业务结果	 SUCCESS/FAIL
    @XmlElement(name = "result_code")
    private String resultCode;
    //错误代码 错误码信息
    @XmlElement(name = "err_code")
    private String errCode;
    //结果信息描述
    @XmlElement(name = "err_code_des")
    private String errCodeDes;
    //商户订单号（每个订单号必须唯一）组成：mch_id+yyyymmdd+10位一天内不能重复的数字
    @XmlElement(name = "mch_billno")
    private String mchBillno;
    //微信支付分配的商户号
    @XmlElement(name = "mch_id")
    private String mchId;
    //商户appid，接口传入的所有appid应该为公众号的appid（在mp.weixin.qq.com申请的），不能为APP的appid（在open.weixin.qq.com申请的）
    @XmlElement(name = "wxappid")
    private String wxappId;
    //接受收红包的用户在wxappid下的openid
    @XmlElement(name = "re_openid")
    private String reOpenid;

    //付款金额，单位分
    @XmlElement(name = "total_amount")
    private String totalAmount;
    //红包订单的微信单号
    @XmlElement(name = "send_listid")
    private String sendListid;
}

