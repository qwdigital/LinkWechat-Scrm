package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.product.QwAddProductQuery;
import com.linkwechat.domain.wecom.query.product.QwProductListQuery;
import com.linkwechat.domain.wecom.query.product.QwProductQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwAddProductVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwProductListVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwProductVo;
import com.linkwechat.fallback.QwProductAlbumFallbackFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author danmo
 * @version 3.0
 * @date 2022/9/9 23:05
 */
@FeignClient(value = "${wecom.serve.linkwe-wecom}", fallback = QwProductAlbumFallbackFactory.class, contextId = "linkwe-wecom-product")
public interface QwProductAlbumClient {

    /**
     * 创建商品图册
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "创建商品图册", httpMethod = "POST")
    @PostMapping("product/add")
    public AjaxResult<QwAddProductVo> addProduct(@RequestBody QwAddProductQuery query);

    /**
     * 编辑商品图册
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "编辑商品图册", httpMethod = "POST")
    @PostMapping("product/update")
    public AjaxResult<WeResultVo> updateProductAlbum(@RequestBody QwAddProductQuery query);

    /**
     * 删除商品图册
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "删除商品图册", httpMethod = "POST")
    @PostMapping("product/delete")
    public AjaxResult<WeResultVo> delProductAlbum(@RequestBody QwProductQuery query);

    /**
     * 获取商品图册
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "获取商品图册", httpMethod = "POST")
    @PostMapping("product/get")
    public AjaxResult<QwProductVo> getProductAlbum(@RequestBody QwProductQuery query);

    /**
     * 获取商品图册列表
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "获取商品图册列表", httpMethod = "POST")
    @PostMapping("product/list")
    public AjaxResult<QwProductListVo> getProductAlbumList(@RequestBody QwProductListQuery query);
}
