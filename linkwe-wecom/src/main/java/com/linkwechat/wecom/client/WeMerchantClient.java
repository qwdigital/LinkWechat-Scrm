package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Post;
import com.linkwechat.domain.wecom.query.merchant.WeGetBillListQuery;
import com.linkwechat.domain.wecom.vo.merchant.WeGetBillListVo;
import com.linkwechat.wecom.interceptor.WeBillAccessTokenInterceptor;

/**
 * 对外收款
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/28 11:29
 */
@BaseRequest(baseURL = "${weComServerUrl}", interceptor = WeBillAccessTokenInterceptor.class)
public interface WeMerchantClient {

    /**
     * 获取对外收款记录
     *
     * @param query
     * @return
     */
    @Post(url = "/externalpay/get_bill_list")
    WeGetBillListVo getBillList(WeGetBillListQuery query);


}
