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
import com.linkwechat.domain.substitute.customer.order.query.WeSubstituteCustomerOrderAddRequest;
import com.linkwechat.domain.substitute.customer.order.query.WeSubstituteCustomerOrderRequest;
import com.linkwechat.domain.substitute.customer.order.query.WeSubstituteCustomerOrderUpdateRequest;
import com.linkwechat.domain.substitute.customer.order.vo.WeSubstituteCustomerOrderVO;
import com.linkwechat.service.IWeSubstituteCustomerOrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
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
     * @param request 列表请求参数
     * @return {@link TableDataInfo}
     * @author WangYX
     * @date 2023/08/03 17:37
     */
    @ApiOperation("列表-分页")
    @GetMapping("")
    public TableDataInfo page(WeSubstituteCustomerOrderRequest request) {
        startPage();
        List<WeSubstituteCustomerOrderVO> list = weSubstituteCustomerOrderService.selectList(request);
        return getDataTable(list);
    }

    /**
     * 新增
     *
     * @param request 新增请求参数
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/08/03 17:54
     */
    @ApiOperation("新增")
    @PostMapping("")
    public AjaxResult add(@RequestBody WeSubstituteCustomerOrderAddRequest request) {
        return AjaxResult.success(weSubstituteCustomerOrderService.add(request));
    }

    /**
     * 修改
     *
     * @param request 修改请求参数
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/08/08 15:49
     */
    @ApiOperation("修改")
    @PutMapping("")
    public AjaxResult update(@Validated @RequestBody WeSubstituteCustomerOrderUpdateRequest request) {
        weSubstituteCustomerOrderService.update(request);
        return AjaxResult.success();
    }

    /**
     * 详情
     *
     * @param id 主键Id
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/08/07 17:02
     */
    @ApiOperation("详情")
    @GetMapping("/{id}")
    public AjaxResult get(@PathVariable("id") Long id) {
        return AjaxResult.success(weSubstituteCustomerOrderService.get(id));
    }


}

