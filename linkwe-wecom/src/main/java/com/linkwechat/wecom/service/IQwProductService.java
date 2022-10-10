package com.linkwechat.wecom.service;

import com.linkwechat.domain.wecom.query.product.QwAddProductQuery;
import com.linkwechat.domain.wecom.query.product.QwProductListQuery;
import com.linkwechat.domain.wecom.query.product.QwProductQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwAddProductVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwProductListVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwProductVo;

public interface IQwProductService {

    QwAddProductVo addProduct(QwAddProductQuery query);

    WeResultVo updateProductAlbum(QwAddProductQuery query);

    WeResultVo delProductAlbum(QwProductQuery query);

    QwProductVo getProductAlbum(QwProductQuery query);

    QwProductListVo getProductAlbumList(QwProductListQuery query);
}
