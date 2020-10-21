package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Request;
import com.linkwechat.wecom.domain.dto.customer.CustomerGroupDetail;
import com.linkwechat.wecom.domain.dto.customer.CustomerGroupList;

/**
 * @description: 客户群
 * @author: HaoN
 * @create: 2020-10-20 21:50
 **/
public interface WeCustomerGroupClient {


    /**
     * 获取客户群列表
     * @param params
     * @return
     */
    @Request(url="/externalcontact/groupchat/list",
            type = "POST"
    )
    CustomerGroupList groupChatLists(@DataObject CustomerGroupList.Params params);


    /**
     * 获取客户群详情
     * @param params
     * @return
     */
    @Request(url="/externalcontact/groupchat/get",
            type = "POST"
    )
    CustomerGroupDetail groupChatDetail(@DataObject CustomerGroupDetail.Params params);
}