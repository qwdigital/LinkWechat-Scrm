package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.wecom.domain.dto.GroupWelcomeTplDto;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.domain.dto.customer.CustomerGroupDetail;
import com.linkwechat.wecom.domain.dto.customer.CustomerGroupList;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * @description: 客户群
 * @author: HaoN
 * @create: 2020-10-20 21:50
 **/
@BaseRequest(baseURL = "${weComServerUrl}${weComePrefix}", interceptor = WeAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
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

    /**
     * 添加入群欢迎语模版
     * @param groupWelcomeTplDto
     * @return
     */
    @Post(url = "/externalcontact/group_welcome_template/add")
    GroupWelcomeTplDto addGroupWelcomeTpl(@JSONBody GroupWelcomeTplDto groupWelcomeTplDto);


    /**
     * 编辑入群欢迎语模版
     * @param groupWelcomeTplDto
     * @return
     */
    @Post(url = "/externalcontact/group_welcome_template/edit")
    WeResultDto updateGroupWelcomeTpl(@JSONBody GroupWelcomeTplDto groupWelcomeTplDto);

}