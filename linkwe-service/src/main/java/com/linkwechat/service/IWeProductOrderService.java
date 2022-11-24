package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.google.protobuf.ServiceException;
import com.linkwechat.domain.WeProductOrder;
import com.linkwechat.domain.product.order.query.WePlaceAnOrderQuery;
import com.linkwechat.domain.product.order.query.WeProductOrderQuery;
import com.linkwechat.domain.product.order.vo.WeProductOrderVo;

import java.util.List;

/**
 * 商品订单表
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/21 18:19
 */
public interface IWeProductOrderService extends IService<WeProductOrder> {

    /**
     * 订单列表
     *
     * @param query
     * @return
     */
    List<WeProductOrderVo> list(WeProductOrderQuery query);


    /**
     * 下单
     *
     * @param query
     * @return
     * @throws ServiceException
     */
    String placeAnOrder(WePlaceAnOrderQuery query) throws ServiceException;


}
