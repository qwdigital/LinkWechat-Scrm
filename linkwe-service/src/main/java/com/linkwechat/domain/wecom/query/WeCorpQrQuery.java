package com.linkwechat.domain.wecom.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 获取加入企业二维码入参
 * @date 2021/12/2 18:27
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeCorpQrQuery extends WeBaseQuery {

    /**
     * qrcode尺寸类型，1: 171 x 171; 2: 399 x 399; 3: 741 x 741; 4: 2052 x 2052
     */
    private Integer size_type = 1;
}
