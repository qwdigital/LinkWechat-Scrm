package com.linkwechat.wecom.controller;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.product.QwAddProductQuery;
import com.linkwechat.domain.wecom.query.product.QwProductListQuery;
import com.linkwechat.domain.wecom.query.product.QwProductQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwAddProductVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwProductListVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwProductVo;
import com.linkwechat.wecom.service.IQwProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author danmo
 * @date 2022/4/18 21:46
 **/
@Api(tags = "商品图册接口管理")
@Slf4j
@RestController
@RequestMapping("product")
public class QwProductController {

    @Resource
    private IQwProductService productService;

    /**
     * 创建商品图册
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "创建商品图册", httpMethod = "POST")
    @PostMapping("/add")
    public AjaxResult<QwAddProductVo> addProduct(@RequestBody QwAddProductQuery query) {
        QwAddProductVo weAddProduct = productService.addProduct(query);
        return AjaxResult.success(weAddProduct);
    }

    /**
     * 编辑商品图册
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "编辑商品图册", httpMethod = "POST")
    @PostMapping("/update")
    public AjaxResult<WeResultVo> updateProductAlbum(@RequestBody QwAddProductQuery query) {
        WeResultVo weResult = productService.updateProductAlbum(query);
        return AjaxResult.success(weResult);
    }

    /**
     * 删除商品图册
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "删除商品图册", httpMethod = "POST")
    @PostMapping("/delete")
    public AjaxResult<WeResultVo> delProductAlbum(@RequestBody QwProductQuery query) {
        WeResultVo weResult = productService.delProductAlbum(query);
        return AjaxResult.success(weResult);
    }

    /**
     * 获取商品图册
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "获取商品图册", httpMethod = "POST")
    @PostMapping("/get")
    public AjaxResult<QwProductVo> getProductAlbum(@RequestBody QwProductQuery query) {
        QwProductVo weProduct = productService.getProductAlbum(query);
        return AjaxResult.success(weProduct);
    }

    /**
     * 获取商品图册列表
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "获取商品图册列表", httpMethod = "POST")
    @PostMapping("/list")
    public AjaxResult<QwProductListVo> getProductAlbumList(@RequestBody QwProductListQuery query) {
        QwProductListVo weProductList = productService.getProductAlbumList(query);
        return AjaxResult.success(weProductList);
    }
}
