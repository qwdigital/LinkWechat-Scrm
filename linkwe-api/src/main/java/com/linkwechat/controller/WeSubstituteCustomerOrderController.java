package com.linkwechat.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.substitute.customer.order.entity.WeSubstituteCustomerOrder;
import com.linkwechat.domain.substitute.customer.order.query.WeSubstituteCustomerOrderRequest;
import com.linkwechat.domain.substitute.customer.order.vo.WeSubstituteCustomerOrderVO;
import com.linkwechat.service.IWeSubstituteCustomerOrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 代客下单-订单 前端控制器
 * </p>
 *
 * @author WangYX
 * @since 2023-08-03
 */
@RestController
@RequestMapping("/weSubstitute/customer/order")
public class WeSubstituteCustomerOrderController extends BaseController {

    @Resource
    private IWeSubstituteCustomerOrderService weSubstituteCustomerOrderService;

    /**
     * 列表-分页
     *
     * @param
     * @return {@link TableDataInfo}
     * @author WangYX
     * @date 2023/08/03 17:37
     */
    @ApiOperation("列表-分页")
    @GetMapping("/page")
    public TableDataInfo page(WeSubstituteCustomerOrderRequest request) {
        startPage();
        LambdaQueryWrapper<WeSubstituteCustomerOrder> queryWrapper = Wrappers.lambdaQuery(WeSubstituteCustomerOrder.class);
        queryWrapper.eq(WeSubstituteCustomerOrder::getDelFlag, Constants.COMMON_STATE);
        queryWrapper.eq(StrUtil.isNotBlank(request.getOrderStatus()), WeSubstituteCustomerOrder::getOrderStatus, request.getOrderStatus());
        List<WeSubstituteCustomerOrder> list = weSubstituteCustomerOrderService.list(queryWrapper);
        TableDataInfo dataTable = getDataTable(list);
        dataTable.setRows(BeanUtil.copyToList(list, WeSubstituteCustomerOrderVO.class));
        return dataTable;
    }

    /**
     * 新增
     *
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/08/03 17:54
     */
    @ApiOperation("新增")
    @PostMapping("/add")
    public AjaxResult add() {

        return AjaxResult.success();
    }

    @ApiOperation("详情")
    @GetMapping("/{id}")
    public AjaxResult get(@PathVariable("id") Long id) {

        return AjaxResult.success();
    }


}

