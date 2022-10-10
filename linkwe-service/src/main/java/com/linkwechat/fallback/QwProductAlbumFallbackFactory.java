package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.product.QwAddProductQuery;
import com.linkwechat.domain.wecom.query.product.QwProductListQuery;
import com.linkwechat.domain.wecom.query.product.QwProductQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwAddProductVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwProductListVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwProductVo;
import com.linkwechat.fegin.QwProductAlbumClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @date 2022/09/09 23:00
 */

@Component
@Slf4j
public class QwProductAlbumFallbackFactory implements QwProductAlbumClient {
    @Override
    public AjaxResult<QwAddProductVo> addProduct(QwAddProductQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> updateProductAlbum(QwAddProductQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> delProductAlbum(QwProductQuery query) {
        return null;
    }

    @Override
    public AjaxResult<QwProductVo> getProductAlbum(QwProductQuery query) {
        return null;
    }

    @Override
    public AjaxResult<QwProductListVo> getProductAlbumList(QwProductListQuery query) {
        return null;
    }
}
