package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.wecom.domain.dto.customer.CustomerGroupDetail;
import com.linkwechat.wecom.domain.dto.customer.CustomerGroupList;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;

/**
 * @description: 客户群
 * @author: HaoN
 * @create: 2020-10-20 21:50
 **/
@BaseRequest(baseURL = "${weComServerUrl}${weComePrefix}", interceptor = WeAccessTokenInterceptor.class)
public interface WeCustomerGroupClient {


    /**
     * 获取客户群列表
     * @param params
     * @return
     */
    @Request(url="/externalcontact/groupchat/list",
            type = "POST"
    )
    CustomerGroupList groupChatLists(@JSONBody CustomerGroupList.Params params);


    /**
     * 获取客户群详情
     * @param params
     * @return
     */
    @Request(url="/externalcontact/groupchat/get",
            type = "POST"
    )
    CustomerGroupDetail groupChatDetail(@JSONBody CustomerGroupDetail.Params params);
}