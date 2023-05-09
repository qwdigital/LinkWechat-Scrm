package com.linkwechat.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.constant.WechatPayUrlConstants;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.wx.coupon.WxCouponListQuery;
import com.linkwechat.domain.wx.coupon.WxSendCouponQuery;
import com.linkwechat.service.IWeCouponService;
import com.linkwechat.wechatpay.WechatPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Objects;

/**
 * @author danmo
 * @date 2023年03月16日 11:26
 */
@Slf4j
@Service
public class IWeCouponServiceImpl implements IWeCouponService {

    @Autowired
    private WechatPayService wechatPayService;


    @Override
    public String getCouponList(WxCouponListQuery query) {
        String result = null;
        try {
            UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(WechatPayUrlConstants.SELECT_COUPON_LIST);
            urlBuilder.queryParam("offset", query.getOffset());
            urlBuilder.queryParam("limit", query.getLimit());
            urlBuilder.queryParam("stock_creator_mchid", query.getStockCreatorMchid());

            if (Objects.nonNull(query.getCreateStartTime())) {
                String startTime = DateUtil.format(query.getCreateStartTime(), "yyyy-MM-dd'T'HH:mm:ssXXX");
                urlBuilder.queryParam("create_start_time", URLEncoder.encode(startTime, "US-ASCII"));
            }
            if (Objects.nonNull(query.getCreateEndTime())) {
                String endTime = DateUtil.format(query.getCreateEndTime(), "yyyy-MM-dd'T'HH:mm:ssXXX");
                urlBuilder.queryParam("create_end_time", URLEncoder.encode(endTime, "US-ASCII"));
            }
            if (StringUtils.isNotBlank(query.getStatus())) {
                urlBuilder.queryParam("status", query.getStatus());
            }
            String url = urlBuilder.build(false).toUriString();
            result = wechatPayService.sendGet(url);
        } catch (Exception e) {
            log.error("条件查询卡券列表异常 query:{} ", JSONObject.toJSONString(query), e);
            throw new WeComException(400, e.getMessage());
        }
        if (StringUtils.isEmpty(result)) {
            throw new WeComException("查询失败");
        }
        return result;
    }

    @Override
    public JSONObject sendCoupon(WxSendCouponQuery query) {
        try {
            //商户id+日期+流水号
            String outRequestNo = query.getStock_creator_mchid() + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN) + RandomUtil.randomNumbers(6);
            query.setOut_request_no(outRequestNo);
            String url = StringUtils.format(WechatPayUrlConstants.SEND_COUPON, query.getOpenid());
            query.setOpenid(null);
            JSONObject params = JSONObject.parseObject(JSONObject.toJSONString(query));
            return wechatPayService.sendPost(url, params);
        } catch (Exception e) {
            log.error("卡券发送失败 query:{}", JSONObject.toJSONString(query), e);
            throw new WeComException("卡券发放失败");
        }
    }
}
