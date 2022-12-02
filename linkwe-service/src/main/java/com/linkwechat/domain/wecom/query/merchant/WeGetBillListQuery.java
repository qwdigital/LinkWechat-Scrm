package com.linkwechat.domain.wecom.query.merchant;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;

/**
 * 对外收款记录
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/28 11:32
 */
@Data
public class WeGetBillListQuery extends WeBaseQuery {

    /**
     * 收款记录的起止时间间隔不能超过1个月
     * 会过滤收款人不在可见范围中的收款记录，因此返回的记录数可能会小于limit设置的最大记录数。
     * 如果得到的列表为空，说明已经拉取完所有的数据。
     */

    /**
     * 是否必填：是
     * 收款记录开始时间
     */
    private Long beginTime;

    /**
     * 是否必填：是
     * 收款记录结束时间
     */
    private Long endTime;

    /**
     * 是否必填：否
     * 企业收款成员userid，不填则为全部成员
     */
    private String payeeUserid;

    /**
     * 是否必填：否
     * 用于分页查询的游标，字符串类型，由上一次调用返回，首次调用可不填
     */
    private String cursor;

    /**
     * 是否必填：否
     * 返回的最大记录数，整型，最大值1000
     */
    private Integer limit;

}
