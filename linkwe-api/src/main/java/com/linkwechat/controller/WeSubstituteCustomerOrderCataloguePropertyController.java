package com.linkwechat.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.substitute.customer.order.entity.WeSubstituteCustomerOrderCatalogueProperty;
import com.linkwechat.domain.substitute.customer.order.query.WeSubstituteCustomerOrderCataloguePropertyRequest;
import com.linkwechat.domain.substitute.customer.order.vo.WeSubstituteCustomerOrderCataloguePropertyVO;
import com.linkwechat.service.IWeSubstituteCustomerOrderCataloguePropertyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 代客下单分类字段 前端控制器
 * </p>
 *
 * @author WangYX
 * @since 2023-08-03
 */
@RestController
@RequestMapping("/substitute/customer/order/property")
public class WeSubstituteCustomerOrderCataloguePropertyController extends BaseController {

    @Resource
    private IWeSubstituteCustomerOrderCataloguePropertyService weSubstituteCustomerOrderCataloguePropertyService;

    /**
     * 列表
     *
     * @param
     * @return {@link TableDataInfo}
     * @author WangYX
     * @date 2023/08/03 9:58
     */
    @ApiOperation("列表")
    @GetMapping("/page")
    public TableDataInfo list(@Validated WeSubstituteCustomerOrderCataloguePropertyRequest request) {
        startPage();
        LambdaQueryWrapper<WeSubstituteCustomerOrderCatalogueProperty> queryWrapper = Wrappers.lambdaQuery(WeSubstituteCustomerOrderCatalogueProperty.class);
        queryWrapper.eq(WeSubstituteCustomerOrderCatalogueProperty::getDelFlag, Constants.COMMON_STATE);
        queryWrapper.eq(WeSubstituteCustomerOrderCatalogueProperty::getCatalogueId, request.getCatalogueId());
        List<WeSubstituteCustomerOrderCatalogueProperty> list = weSubstituteCustomerOrderCataloguePropertyService.list(queryWrapper);
        TableDataInfo dataTable = getDataTable(list);
        dataTable.setRows(BeanUtil.copyToList(list, WeSubstituteCustomerOrderCataloguePropertyVO.class));
        return dataTable;
    }

}

