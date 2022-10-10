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
 * 发送红包
 */
@Data
@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeRedEnvelopesParmDto {
    //商户账号appid
    @XmlElement(name = "mch_appid",required = true)
    private String mch_appid;
    //商户号
    @XmlElement(name = "mchid",required = true)
    private String mchid;
    //设备号
    @XmlElement(name = "device_info")
    private String device_info;
    //随机字符串
    @XmlElement(name = "nonce_str",required = true)
    private String nonce_str;
    //签名
    @XmlElement(name = "sign",required = true)
    private String sign;
    //商户订单号
    @XmlElement(name = "partner_trade_no",required = true)
    private String partner_trade_no;
    //用户openid
    @XmlElement(name = "openid",required = true)
    private String openid;
    //校验用户姓名选项
    @XmlElement(name = "check_name",required = true)
    private String check_name;
    //收款用户姓名
    @XmlElement(name = "re_user_name")
    private String re_user_name;
    //金额
    @XmlElement(name = "amount",required = true)
    private Integer amount;
    //付款备注
    @XmlElement(name = "desc",required = true)
    private String desc;
    //Ip地址
    @XmlElement(name = "spbill_create_ip")
    private String spbill_create_ip;
    //付款场景
    @XmlElement(name = "scene")
    private Integer scene;
    //品牌ID
    @XmlElement(name = "brand_id")
    private Integer brand_id;
    //消息模板ID
    @XmlElement(name = "finder_template_id")
    private  String  finder_template_id;




}

