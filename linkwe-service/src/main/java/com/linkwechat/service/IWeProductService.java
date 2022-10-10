package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeProduct;
import com.linkwechat.domain.product.query.WeAddProductQuery;
import com.linkwechat.domain.product.query.WeProductQuery;
import com.linkwechat.domain.product.vo.WeProductListVo;
import com.linkwechat.domain.product.vo.WeProductVo;

import java.util.List;

/**
 * 商品信息表(WeProduct)
 *
 * @author danmo
 * @since 2022-09-30 11:36:06
 */
public interface IWeProductService extends IService<WeProduct> {

    /**
     * 新增商品
     * @param query
     */
    void addProduct(WeAddProductQuery query);

    /**
     * 修改商品
     * @param id 商品ID
     * @param query 商品信息
     */
    void updateProduct(Long id, WeAddProductQuery query);

    /**
     * 删除商品
     * @param ids 商品ID
     */
    void delProduct(List<Long> ids);

    /**
     * 商品详情
     * @param id 商品ID
     */
    WeProductVo getProduct(Long id);

    /**
     * 获取商品列表
     * @param query 筛选条件
     * @return
     */
    List<WeProductListVo> productList(WeProductQuery query);

    /**
     * 同步商品列表
     */
    void syncProductList();

    void syncProductListHandle(String msg);

}
