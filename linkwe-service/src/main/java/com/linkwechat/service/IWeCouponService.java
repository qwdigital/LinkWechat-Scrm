package com.linkwechat.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.linkwechat.domain.WeLxQrCode;
import com.linkwechat.domain.qr.query.WeLxQrAddQuery;
import com.linkwechat.domain.qr.query.WeLxQrCodeListQuery;
import com.linkwechat.domain.qr.query.WxLxQrQuery;
import com.linkwechat.domain.qr.vo.*;
import com.linkwechat.domain.wx.coupon.WxCouponListQuery;
import com.linkwechat.domain.wx.coupon.WxSendCouponQuery;

import java.util.List;

/**
 * 卡券业务接口
 *
 * @author danmo
 * @since 2023-03-07 14:59:35
 */
public interface IWeCouponService {

    /**
     * 查询卡券批次列表
     * @param query
     * @return
     */
    String getCouponList(WxCouponListQuery query);

    /**
     * 发放卡券
     */
    JSONObject sendCoupon(WxSendCouponQuery query);
}
