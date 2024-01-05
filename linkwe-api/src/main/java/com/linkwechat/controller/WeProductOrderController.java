package com.linkwechat.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.enums.ProductOrderStateEnum;
import com.linkwechat.common.enums.ProductRefundOrderStateEnum;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeProductOrder;
import com.linkwechat.domain.product.order.query.WeProductOrderQuery;
import com.linkwechat.domain.product.order.vo.WeProductOrderPayInfoVo;
import com.linkwechat.domain.product.order.vo.WeProductOrderVo;
import com.linkwechat.domain.product.order.vo.WeProductOrderWareVo;
import com.linkwechat.domain.product.refund.vo.WeProductOrderRefundVo;
import com.linkwechat.service.IWeProductOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 商品订单
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/21 18:22
 */
@RestController
@RequestMapping(value = "product/order")
@Api(tags = "商品订单")
public class WeProductOrderController extends BaseController {

    @Resource
    private IWeProductOrderService weProductOrderService;

    @ApiOperation(value = "商品订单列表", httpMethod = "GET")
    @Log(title = "商品订单列表", businessType = BusinessType.SELECT)
    @GetMapping("/page/list")
    public TableDataInfo<WeProductOrderVo> list(WeProductOrderQuery query) {
        startPage();
        List<WeProductOrderWareVo> list = weProductOrderService.list(query);
        List<WeProductOrderVo> result = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (WeProductOrderWareVo weProductOrderWareVo : list) {
                WeProductOrderVo weProductOrderVo = BeanUtil.copyProperties(weProductOrderWareVo, WeProductOrderVo.class);
                //订单状态
                weProductOrderVo.setOrderStateStr(ProductOrderStateEnum.of(weProductOrderVo.getOrderState()).getMsg());
                //订单金额
                BigDecimal totalFee = new BigDecimal(weProductOrderVo.getTotalFee()).divide(BigDecimal.valueOf(100L)).setScale(2, BigDecimal.ROUND_HALF_UP);
                weProductOrderVo.setTotalFee(totalFee.toString());
                List<WeProductOrderRefundVo> refunds = weProductOrderWareVo.getRefunds();
                if (refunds != null && refunds.size() > 0) {
                    refunds.sort(Comparator.comparing(WeProductOrderRefundVo::getRefundTime).reversed());
                    WeProductOrderRefundVo weProductOrderRefundVo = refunds.get(0);
                    //退款订单金额
                    BigDecimal refundFee = new BigDecimal(weProductOrderRefundVo.getRefundFee()).divide(BigDecimal.valueOf(100L)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    weProductOrderVo.setRefundFee(refundFee.toString());
                    //退款订单状态
                    weProductOrderVo.setRefundStateStr(ProductRefundOrderStateEnum.of(weProductOrderRefundVo.getRefundState()).getMsg());
                }
                //客户类型
                if(weProductOrderVo.getExternalType()!=null){
                    weProductOrderVo.setExternalTypeStr(weProductOrderVo.getExternalType() == 1 ? "微信" : "企业微信");
                }else{
                    weProductOrderVo.setExternalTypeStr("未知");
                }

                result.add(weProductOrderVo);
            }
        }
        return getDataTable(result);
    }

    @ApiOperation(value = "查询订单交易状态", httpMethod = "GET")
    @Log(title = "订单交易状态", businessType = BusinessType.SELECT)
    @GetMapping("/trading/status/{orderNo}")
    public AjaxResult<WeProductOrderPayInfoVo> orderTradingStatus(@PathVariable("orderNo") String orderNo) {
        LambdaQueryWrapper<WeProductOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(WeProductOrder::getPayTime, WeProductOrder::getOrderNo, WeProductOrder::getMchNo);
        queryWrapper.eq(WeProductOrder::getOrderNo, orderNo);
        queryWrapper.eq(WeProductOrder::getDelFlag, 0);
        WeProductOrder weProductOrder = weProductOrderService.getOne(queryWrapper);
        WeProductOrderPayInfoVo weProductOrderPayInfoVo = null;
        if (ObjectUtil.isNotEmpty(weProductOrder)) {
            weProductOrderPayInfoVo = BeanUtil.copyProperties(weProductOrder, WeProductOrderPayInfoVo.class);
        }
        return AjaxResult.success(weProductOrderPayInfoVo);
    }

    /**
     * 导出Excel，仅导出查询范围内的数据，最多支持导出一年的数据
     *
     * @param query
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2022/11/22 10:16
     */
    @ApiOperation(value = "导出Excel", httpMethod = "GET")
    @Log(title = "导出Excel", businessType = BusinessType.SELECT)
    @GetMapping("/export")
    public void export(WeProductOrderQuery query) throws IOException {
        if (StringUtils.isNotBlank(query.getBeginTime()) && StringUtils.isNotBlank(query.getEndTime())) {
            //判断时间间隔
            long between = DateUtil.between(DateUtil.parse(query.getBeginTime()), DateUtil.parse(query.getEndTime()), DateUnit.DAY);
            long yearOfDays = 365L;
            if (between > yearOfDays) {
                throw new ServiceException("最多支持导出一年的数据");
            }
        } else {
            //默认导出一个月的数据
            DateTime dateTime = DateUtil.offsetMonth(new Date(), -1);
            query.setBeginTime(DateUtil.format(dateTime, "yyyy-MM-dd"));
            query.setEndTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        }
        List<WeProductOrderWareVo> list = weProductOrderService.list(query);
        List<WeProductOrderVo> result = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (WeProductOrderWareVo weProductOrderWareVo : list) {
                WeProductOrderVo weProductOrderVo = BeanUtil.copyProperties(weProductOrderWareVo, WeProductOrderVo.class);
                //订单状态
                weProductOrderVo.setOrderStateStr(ProductOrderStateEnum.of(weProductOrderVo.getOrderState()).getMsg());
                //订单金额
                BigDecimal totalFee = new BigDecimal(weProductOrderVo.getTotalFee()).divide(BigDecimal.valueOf(100L)).setScale(2, BigDecimal.ROUND_HALF_UP);
                weProductOrderVo.setTotalFee(totalFee.toString());
                List<WeProductOrderRefundVo> refunds = weProductOrderWareVo.getRefunds();
                if (refunds != null && refunds.size() > 0) {
                    refunds.sort(Comparator.comparing(WeProductOrderRefundVo::getRefundTime).reversed());
                    WeProductOrderRefundVo weProductOrderRefundVo = refunds.get(0);
                    //退款订单金额
                    BigDecimal refundFee = new BigDecimal(weProductOrderRefundVo.getRefundFee()).divide(BigDecimal.valueOf(100L)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    weProductOrderVo.setRefundFee(refundFee.toString());
                    //退款订单状态
                    weProductOrderVo.setRefundStateStr(ProductRefundOrderStateEnum.of(weProductOrderRefundVo.getRefundState()).getMsg());
                }
                //客户类型
                weProductOrderVo.setExternalTypeStr(weProductOrderVo.getExternalType() == 1 ? "微信" : "企业微信");
                result.add(weProductOrderVo);
            }
        }
        HttpServletResponse response = ServletUtils.getResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("商品订单", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), WeProductOrderVo.class).sheet("商品订单").doWrite(result);
    }

    @ApiOperation(value = "同步订单", httpMethod = "GET")
    @Log(title = "同步订单", businessType = BusinessType.SELECT)
    @GetMapping("/orderSync")
    public AjaxResult orderSync() {
        weProductOrderService.orderSync();
        return AjaxResult.success();
    }

}
