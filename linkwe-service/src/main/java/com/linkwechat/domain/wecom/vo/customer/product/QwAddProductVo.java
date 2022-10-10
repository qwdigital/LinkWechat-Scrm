package com.linkwechat.domain.wecom.vo.customer.product;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 创建商品图册
 * @date 2021/12/2 16:11
 **/

@EqualsAndHashCode(callSuper = true)
@Data
public class QwAddProductVo extends WeResultVo {

    /**
     * 商品id
     */
    private String productId;

}
