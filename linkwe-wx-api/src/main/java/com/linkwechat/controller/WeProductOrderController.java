package com.linkwechat.controller;

import com.google.protobuf.ServiceException;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.domain.product.order.query.WePlaceAnOrderQuery;
import com.linkwechat.service.IWeProductOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 订单H5
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/24 9:33
 */
@RestController
@RequestMapping(value = "order")
@Api(tags = "商品订单")
public class WeProductOrderController extends BaseController {

    @Resource
    private IWeProductOrderService weProductOrderService;

    @ApiOperation(value = "下单", httpMethod = "POST")
    @Log(title = "下单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult placeAnOrder(@RequestBody @Validated WePlaceAnOrderQuery query) throws ServiceException {

        String s = weProductOrderService.placeAnOrder(query);

        return AjaxResult.success();
    }

}
