package com.linkwechat.domain.wecom.query.product;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;

/**
 * 获取商品图册入参
 *
 * @author danmo
 */
@Data
public class QwProductListQuery extends WeBaseQuery {

    /**
     * 返回的最大记录数，整型，最大值100，默认值50，超过最大值时取默认值
     */
    private Integer limit = 100;

    /**
     * 用于分页查询的游标，字符串类型，由上一次调用返回，首次调用可不填
     */
    private String cursor;
}
