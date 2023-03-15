package com.linkwechat.controller;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.constant.WechatPayUrlConstants;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.wx.coupon.WxCouponListQuery;
import com.linkwechat.wechatpay.WechatPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.util.Objects;

/**
 * 微信支付卡券
 *
 * @author danmo
 * @date 2023年03月11日 16:57
 */

@Api(tags = "微信支付卡券")
@Slf4j
@RestController
@RequestMapping("/coupon")
public class WeCouponController extends BaseController {


    @Autowired
    private WechatPayService wechatPayService;


    @ApiOperation(value = "条件查询卡券列表", httpMethod = "GET")
    @GetMapping("/getCouponList")
    public TableDataInfo getCouponList(WxCouponListQuery query) {
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
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONArray list = jsonObject.getJSONArray("data");
        TableDataInfo dataTable = getDataTable(list);
        dataTable.setTotal(jsonObject.getLong("total_count"));
        return dataTable;
    }

}
