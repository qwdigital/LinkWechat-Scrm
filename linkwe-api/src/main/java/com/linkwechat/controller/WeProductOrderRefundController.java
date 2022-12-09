package com.linkwechat.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.enums.ProductRefundOrderStateEnum;
import com.linkwechat.domain.WeProductOrderRefund;
import com.linkwechat.domain.product.refund.vo.WeProductOrderRefundVo;
import com.linkwechat.service.IWeProductOrderRefundService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品订单退款表 前端控制器
 * </p>
 *
 * @author WangYX
 * @since 2022-11-22
 */
@Api(tags = "商品订单退款")
@RestController
@RequestMapping("/product/refund")
public class WeProductOrderRefundController extends BaseController {

    @Resource
    private IWeProductOrderRefundService weProductOrderRefundService;

    @ApiOperation(value = "查询退款订单交易状态", httpMethod = "GET")
    @Log(title = "退款订单交易状态", businessType = BusinessType.SELECT)
    @GetMapping("/refund/status/{orderNo}")
    public AjaxResult<List<WeProductOrderRefundVo>> refundStatus(@PathVariable("orderNo") String orderNo) {
        LambdaQueryWrapper<WeProductOrderRefund> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeProductOrderRefund::getOrderNo, orderNo);
        queryWrapper.eq(WeProductOrderRefund::getDelFlag, 0);
        List<WeProductOrderRefund> list = weProductOrderRefundService.list(queryWrapper);
        List<WeProductOrderRefundVo> weProductOrderRefundVos = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (WeProductOrderRefund one : list) {
                WeProductOrderRefundVo weProductOrderRefundVo = BeanUtil.copyProperties(one, WeProductOrderRefundVo.class);
                BigDecimal bigDecimal = new BigDecimal(weProductOrderRefundVo.getRefundFee()).divide(BigDecimal.valueOf(100L)).setScale(2, BigDecimal.ROUND_HALF_UP);
                weProductOrderRefundVo.setRefundFee(bigDecimal.toString());
                weProductOrderRefundVo.setRefundStateStr(ProductRefundOrderStateEnum.of(weProductOrderRefundVo.getRefundState()).getMsg());
                weProductOrderRefundVos.add(weProductOrderRefundVo);
            }
        }
        return AjaxResult.success(weProductOrderRefundVos);
    }
}

