package com.linkwechat.wecom.client;


import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Request;
import com.dtflys.forest.annotation.Retry;
import com.linkwechat.domain.wecom.query.groupmsg.WeGroupMsgQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.goupmsg.WeGroupMsgTplVo;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;


/**
 * 群欢迎语模版
 */
@BaseRequest(baseURL = "${weComServerUrl}", interceptor = WeAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeGroupWelcomeTplClient {


    /**
     * 添加入群欢迎语素材
     * @param query
     * @return
     */
    @Request(url = "/externalcontact/group_welcome_template/add", type = "POST")
    WeGroupMsgTplVo addWeGroupMsg(@JSONBody WeGroupMsgQuery query);


    /**
     * 编辑入群欢迎语素材
     * @param query
     * @return
     */
    @Request(url = "/externalcontact/group_welcome_template/edit", type = "POST")
    WeResultVo updateWeGroupMsg(@JSONBody WeGroupMsgQuery query);



    /**
     * 删除入群欢迎语素材
     * @param query
     * @return
     */
    @Request(url = "/externalcontact/group_welcome_template/del", type = "POST")
    WeResultVo delWeGroupMsg(@JSONBody WeGroupMsgQuery query);

}
