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
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeProductOrder;
import com.linkwechat.domain.product.order.query.WePlaceAnOrderQuery;
import com.linkwechat.domain.product.order.query.WeProductOrderQuery;
import com.linkwechat.domain.product.order.vo.WeProductOrderPayInfoVo;
import com.linkwechat.domain.product.order.vo.WeProductOrderVo;
import com.linkwechat.service.IWeProductOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
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
        List<WeProductOrderVo> list = weProductOrderService.list(query);
        return getDataTable(list);
    }

    @ApiOperation(value = "查询订单交易状态", httpMethod = "GET")
    @Log(title = "订单交易状态", businessType = BusinessType.SELECT)
    @GetMapping("/trading/status/{orderId}")
    public AjaxResult<WeProductOrderPayInfoVo> orderTradingStatus(@PathVariable("orderId") Long orderId) {
        LambdaQueryWrapper<WeProductOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(WeProductOrder::getPayTime, WeProductOrder::getOrderNo, WeProductOrder::getMchNo);
        queryWrapper.eq(WeProductOrder::getId, orderId);
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
        List<WeProductOrderVo> list = weProductOrderService.list(query);
        HttpServletResponse response = ServletUtils.getResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("商品订单", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), WeProductOrderVo.class).sheet("商品订单").doWrite(list);
    }




}
