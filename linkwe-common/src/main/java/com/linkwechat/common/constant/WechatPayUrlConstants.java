package com.linkwechat.common.constant;

/**
 * 微信支付API地址
 * @author danmo
 * @date 2023年03月15日 14:03
 */
public class WechatPayUrlConstants {

    //发放代金券批次API
    public final static String SEND_COUPON = "https://api.mch.weixin.qq.com/v3/marketing/favor/users/{}/coupons";

    //发放代金券批次API
    public final static String SELECT_COUPON_LIST = "https://api.mch.weixin.qq.com/v3/marketing/favor/stocks";

    //查询批次详情API
    public final static String SELECT_COUPON_DETAIL = "https://api.mch.weixin.qq.com/v3/marketing/favor/stocks/{}";

    //查询代金券详情API
    public final static String SELECT_COUPON_CUSTOMER_DETAIL = "https://api.mch.weixin.qq.com/v3/marketing/favor/users/{}/coupons/{}";


}
