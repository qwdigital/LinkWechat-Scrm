package com.linkwechat.common.enums;

import lombok.Getter;

import java.util.stream.Stream;

//红包状态码
@Getter
public enum RedEnvelopesReturnStatus {

    RETURN_STATUS_NO_AUTH("NO_AUTH","暂无接口权限，请检查账号。","1. 用户账号被冻结，无法付款2. 产品权限没有开通或者被风控冻结3. 此IP地址不允许调用接口，如有需要请登录微信支付商户平台更改配置"),
    RETURN_STATUS_AMOUNT_LIMIT("AMOUNT_LIMIT","红包金额超限，请检查。","1. 被微信风控拦截，最低单笔付款限额调整为5元。2. 低于最低单笔付款限额或者高于最高单笔付款限额。"),
    RETURN_STATUS_PARAM_ERROR("PARAM_ERROR","请求参数错误，请检查。","1. 请求参数校验错误2. 字符中包含非utf8字符3. 商户号和appid没有绑定关系"),
    RETURN_STATUS_OPENID_ERROR("OPENID_ERROR","公众账号Openid有误，请检查。","Openid格式错误或者不属于商家公众账号"),
    RETURN_STATUS_SEND_FAILED("SEND_FAILED","付款错误，请检查。","付款错误，请查单确认付款结果"),
    RETURN_STATUS_NOTENOUGH("NOTENOUGH","商户余额不足，请检查。","您的付款账号余额不足或资金未到账"),
    RETURN_STATUS_SYSTEMERROR("SYSTEMERROR","系统繁忙，请稍后再试。","微信内部接口调用发生错误"),
    RETURN_STATUS_NAME_MISMATCH("NAME_MISMATCH","收款人身份校验有误，请检查。","收款人身份校验不通过"),
    RETURN_STATUS_SIGN_ERROR("SIGN_ERROR","检验前面有误，请检查。","校验签名错误"),
    RETURN_STATUS_XML_ERROR("XML_ERROR","请求数据不合法，请检查。","Post请求数据不是合法的xml格式内容"),
    RETURN_STATUS_FATAL_ERROR("FATAL_ERROR","两次请求参数不一致，请检查。","两次请求商户单号一样，但是参数不一致"),
    RETURN_STATUS_FREQ_LIMIT("FREQ_LIMIT","超过频率限制，请稍后再试。","接口请求频率超时接口限制"),
    RETURN_STATUS_MONEY_LIMIT("MONEY_LIMIT","已达今日付款总额上限/已达用户收款额度上限。","请关注接口的付款限额条件"),
    RETURN_STATUS_CA_ERROR("CA_ERROR","商户API证书校验出错，请检查。","请求没带商户API证书或者带上了错误的商户API证书"),
    RETURN_STATUS_V2_ACCOUNT_SIMPLE_BAN("V2_ACCOUNT_SIMPLE_BAN","未实名收款账户无法付款，请检查。","用户微信支付账户未实名，无法付款"),
    RETURN_STATUS_PARAM_IS_NOT_UTF8("PARAM_IS_NOT_UTF8","请求参数编码有误，请检查。","接口规范要求所有请求参数都必须为utf8编码"),
    RETURN_STATUS_SENDNUM_LIMIT("SENDNUM_LIMIT","今日付款次数超限，请检查。","该用户今日付款次数超过限制,如有需要请进入【微信支付商户平台-产品中心-付款到零钱-产品设置】进行修改"),
    RETURN_STATUS_RECV_ACCOUNT_NOT_ALLOWED("RECV_ACCOUNT_NOT_ALLOWED","收款账户不在收款账户列表，请检查。","收款账户不在收款账户列表"),
    RETURN_STATUS_PAY_CHANNEL_NOT_ALLOWED("PAY_CHANNEL_NOT_ALLOWED","商户未配置API发起能力，请开通。","本商户号未配置API发起能力"),
    RETURN_STATUS_SEND_MONEY_LIMIT("SEND_MONEY_LIMIT","已达今日商户付款额度上限，请检查。","请提高商户付款额度，或明日再试"),
    RETURN_STATUS_RECEIVED_MONEY_LIMIT("RECEIVED_MONEY_LIMIT","已达今日用户收款额度上限，请检查。","请提高用户收款额度，或明日再试"),
    RETURN_STATUS_REFUND("REFUND","红包超过24小时未领取","红包超过24小时未领取");


    private String code;

    private String desc;

    private String reason;

    RedEnvelopesReturnStatus(String code,String desc,String reason){
        this.code=code;
        this.desc=desc;
        this.reason=reason;
    }

    public static  RedEnvelopesReturnStatus of(String type){
        return Stream.of(values()).filter(s->s.getCode().equals(type)).findFirst().orElseGet(null);
    }


}

