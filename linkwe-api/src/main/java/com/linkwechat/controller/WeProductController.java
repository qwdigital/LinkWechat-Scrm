package com.linkwechat.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeProduct;
import com.linkwechat.domain.product.product.query.WeProductLineChartQuery;
import com.linkwechat.domain.product.product.vo.WeProductListVo;
import com.linkwechat.domain.product.product.vo.WeProductStatisticsVo;
import com.linkwechat.domain.product.product.vo.WeProductVo;
import com.linkwechat.domain.product.product.vo.WeUserOrderTop5Vo;
import com.linkwechat.domain.product.query.WeAddProductQuery;
import com.linkwechat.domain.product.query.WeProductQuery;
import com.linkwechat.service.IWeProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author danmo
 * @description 商品图册管理
 * @date 2022/9/12 18:22
 **/

@RestController
@RequestMapping(value = "product")
@Api(tags = "商品图册管理")
public class WeProductController extends BaseController {

    @Autowired
    private IWeProductService weProductService;

    @ApiOperation(value = "新增商品", httpMethod = "POST")
    @Log(title = "新增商品", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addProduct(@RequestBody @Validated WeAddProductQuery query) {
        weProductService.addProduct(query);
        return AjaxResult.success();
    }

    @ApiOperation(value = "修改商品", httpMethod = "PUT")
    @Log(title = "修改商品", businessType = BusinessType.UPDATE)
    @PutMapping("/update/{id}")
    public AjaxResult updateProduct(@PathVariable("id") Long id, @RequestBody @Validated WeAddProductQuery query) {
        weProductService.updateProduct(id, query);
        return AjaxResult.success();
    }

    @ApiOperation(value = "删除商品", httpMethod = "DELETE")
    @Log(title = "删除商品", businessType = BusinessType.UPDATE)
    @DeleteMapping("/delete/{ids}")
    public AjaxResult delProduct(@PathVariable("ids") List<Long> ids) {
        weProductService.delProduct(ids);
        return AjaxResult.success();
    }

    @ApiOperation(value = "商品详情", httpMethod = "GET")
    @Log(title = "商品详情", businessType = BusinessType.SELECT)
    @GetMapping("/get/{id}")
    public AjaxResult<WeProductVo> getProduct(@PathVariable("id") Long id) {
        WeProductVo product = weProductService.getProduct(id);
        return AjaxResult.success(product);
    }

    @ApiOperation(value = "商品列表", httpMethod = "GET")
    @Log(title = "商品列表", businessType = BusinessType.SELECT)
    @GetMapping("/page/list")
    public TableDataInfo<WeProductListVo> productPageList(WeProductQuery query) {
        startPage();
        List<WeProductListVo> list = weProductService.productList(query);
        list.forEach(o -> {
            if (!o.getOrderTotalAmount().equals("0.0")) {
                BigDecimal bigDecimal = new BigDecimal(o.getOrderTotalAmount());
                bigDecimal = bigDecimal.divide(BigDecimal.valueOf(100L)).setScale(2, BigDecimal.ROUND_HALF_UP);
                o.setOrderTotalAmount(bigDecimal.toString());
            }
        });
        return getDataTable(list);
    }

    @ApiOperation(value = "商品列表-移动端", httpMethod = "GET")
    @Log(title = "商品列表-移动端", businessType = BusinessType.SELECT)
    @GetMapping("/list")
    public TableDataInfo<WeProductListVo> list(WeProductQuery query) {
        startPage();
        LambdaQueryWrapper<WeProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(query.getName()), WeProduct::getDescribe, query.getName());
        List<WeProduct> list = weProductService.list(queryWrapper);
        List<WeProductListVo> result = new ArrayList<>();
        list.forEach(o -> {
            WeProductListVo weProductListVo = BeanUtil.copyProperties(o, WeProductListVo.class);
            BigDecimal bigDecimal = new BigDecimal(weProductListVo.getPrice());
            bigDecimal = bigDecimal.divide(BigDecimal.valueOf(100L)).setScale(2, BigDecimal.ROUND_HALF_UP);
            weProductListVo.setPrice(bigDecimal.toString());
            result.add(weProductListVo);
        });
        return getDataTable(result);
    }

    @ApiOperation(value = "商品详情统计", httpMethod = "GET")
    @Log(title = "商品详情统计", businessType = BusinessType.SELECT)
    @GetMapping("/statistics/{id}")
    public AjaxResult statistics(@PathVariable("id") Long id) {
        WeProductStatisticsVo statistics = weProductService.statistics(id);
        return AjaxResult.success(statistics);
    }

    @ApiOperation(value = "商品详情折线图", httpMethod = "POST")
    @Log(title = "商品详情折线图", businessType = BusinessType.SELECT)
    @PostMapping("/lineChart")
    public AjaxResult lineChart(@RequestBody WeProductLineChartQuery query) {
        Map<String, Object> map = weProductService.lineChart(query);
        return AjaxResult.success(map);
    }

    @ApiOperation(value = "员工订单Top5", httpMethod = "GET")
    @Log(title = "员工订单Top5", businessType = BusinessType.SELECT)
    @GetMapping("/top5/{id}")
    public AjaxResult<List<WeUserOrderTop5Vo>> top5(@PathVariable("id") Long id) {
        List<WeUserOrderTop5Vo> weUserOrderTop5Vos = weProductService.userOrderTop5(id);
        return AjaxResult.success(weUserOrderTop5Vos);
    }
}
