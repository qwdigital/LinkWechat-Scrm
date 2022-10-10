package com.linkwechat.controller;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.domain.product.query.WeAddProductQuery;
import com.linkwechat.domain.product.query.WeProductQuery;
import com.linkwechat.domain.product.vo.WeProductListVo;
import com.linkwechat.domain.product.vo.WeProductVo;
import com.linkwechat.service.IWeProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return getDataTable(list);
    }

    @ApiOperation(value = "商品列表同步", httpMethod = "GET")
    @Log(title = "商品列表同步", businessType = BusinessType.OTHER)
    @GetMapping("/list/sync")
    public AjaxResult syncProductList() {
        weProductService.syncProductList();
        return AjaxResult.success();
    }
}
