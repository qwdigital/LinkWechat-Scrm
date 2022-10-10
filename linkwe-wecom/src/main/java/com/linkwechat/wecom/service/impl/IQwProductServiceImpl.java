package com.linkwechat.wecom.service.impl;

import com.linkwechat.domain.wecom.query.product.QwAddProductQuery;
import com.linkwechat.domain.wecom.query.product.QwProductListQuery;
import com.linkwechat.domain.wecom.query.product.QwProductQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwAddProductVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwProductListVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwProductVo;
import com.linkwechat.wecom.client.WeCustomerClient;
import com.linkwechat.wecom.service.IQwProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author danmo
 * @description 商品图册
 * @date 2022/9/9 23:35
 **/
@Service
public class IQwProductServiceImpl implements IQwProductService {

    @Resource
    private WeCustomerClient weCustomerClient;

    @Override
    public QwAddProductVo addProduct(QwAddProductQuery query) {
        query.setAttachments(query.getMessageTemplates());
        return weCustomerClient.addProductAlbum(query);
    }

    @Override
    public WeResultVo updateProductAlbum(QwAddProductQuery query) {
        query.setAttachments(query.getMessageTemplates());
        return weCustomerClient.updateProductAlbum(query);
    }

    @Override
    public WeResultVo delProductAlbum(QwProductQuery query) {
        return weCustomerClient.delProductAlbum(query);
    }

    @Override
    public QwProductVo getProductAlbum(QwProductQuery query) {
        return weCustomerClient.getProductAlbum(query);
    }

    @Override
    public QwProductListVo getProductAlbumList(QwProductListQuery query) {
        return weCustomerClient.getProductAlbumList(query);
    }
}
