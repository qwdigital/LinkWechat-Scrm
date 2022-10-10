package com.linkwechat.domain.wecom.vo.customer.product;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 创建商品图册
 * @date 2021/12/2 16:11
 **/

@EqualsAndHashCode(callSuper = true)
@Data
public class QwProductVo extends WeResultVo {

    private QwProduct product;

    @Data
    public static class QwProduct{
        /**
         * 商品id
         */
        private String productId;

        /**
         * 商品的名称、特色等;不超过300个字
         */
        private String description;

        /**
         * 商品编码；不超过128个字节；只能输入数字和字母
         */
        private String productSn;

        /**
         * 商品的价格，单位为分
         */
        private Long price;

        /**
         * 商品图册创建时间
         */
        private Long createTime;

        /**
         * 附件类型
         */
        private List<JSONObject> attachments;
    }
}
