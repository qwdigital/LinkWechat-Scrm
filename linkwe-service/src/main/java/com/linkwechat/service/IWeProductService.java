package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.google.protobuf.ServiceException;
import com.linkwechat.domain.WeProduct;
import com.linkwechat.domain.product.product.query.WeAddProductQuery;
import com.linkwechat.domain.product.product.query.WeProductLineChartQuery;
import com.linkwechat.domain.product.product.query.WeProductQuery;
import com.linkwechat.domain.product.product.vo.WeProductListVo;
import com.linkwechat.domain.product.product.vo.WeProductStatisticsVo;
import com.linkwechat.domain.product.product.vo.WeProductVo;
import com.linkwechat.domain.product.product.vo.WeUserOrderTop5Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品信息表(WeProduct)
 *
 * @author danmo
 * @since 2022-09-30 11:36:06
 */
public interface IWeProductService extends IService<WeProduct> {

    /**
     * 新增商品
     *
     * @param query
     */
    void addProduct(WeAddProductQuery query) throws ServiceException;

    /**
     * 修改商品
     *
     * @param id    商品ID
     * @param query 商品信息
     */
    void updateProduct(Long id, WeAddProductQuery query) throws ServiceException;

    /**
     * 删除商品
     *
     * @param ids 商品ID
     */
    void delProduct(List<Long> ids);

    /**
     * 商品详情
     *
     * @param id 商品ID
     */
    WeProductVo getProduct(Long id);

    /**
     * 获取商品列表
     *
     * @param query 筛选条件
     * @return
     */
    List<WeProductListVo> productList(WeProductQuery query);

    /**
     * 同步商品列表
     */
    void syncProductList();

    /**
     * 执行商品同步
     *
     * @param msg
     */
    void syncProductListHandle(String msg);

    /**
     * 订单统计
     *
     * @param productId
     * @return
     */
    WeProductStatisticsVo statistics(Long productId);


    /**
     * 折现图统计
     *
     * @param query
     * @return
     */
    Map<String, Object> lineChart(WeProductLineChartQuery query);

    /**
     * 员工订单TOP5
     *
     * @param productId
     * @return
     */
    List<WeUserOrderTop5Vo> userOrderTop5(Long productId);


}
