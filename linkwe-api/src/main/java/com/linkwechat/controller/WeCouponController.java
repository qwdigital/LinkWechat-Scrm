package com.linkwechat.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.wx.coupon.WxCouponListQuery;
import com.linkwechat.service.IWeCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private IWeCouponService weCouponService;


    @ApiOperation(value = "条件查询卡券列表", httpMethod = "GET")
    @GetMapping("/getCouponList")
    public TableDataInfo getCouponList(WxCouponListQuery query) {
        String couponList = weCouponService.getCouponList(query);
        JSONObject jsonObject = JSONObject.parseObject(couponList);
        JSONArray list = jsonObject.getJSONArray("data");
        TableDataInfo dataTable = getDataTable(list);
        dataTable.setTotal(jsonObject.getLong("total_count"));
        return dataTable;
    }

}
