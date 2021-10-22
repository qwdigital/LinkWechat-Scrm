package com.linkwechat.wecom.client;


import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.PostRequest;
import com.dtflys.forest.annotation.Retry;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.domain.dto.moments.MomentsParamDto;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;


/**
 * 朋友圈
 */
@BaseRequest(baseURL = "${weComServerUrl}${weComePrefix}",retryer = WeCommonRetryWhen.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeMomentsClient {


    /**
     * 创建朋友圈
     * @param momentsParamDto
     * @return
     */
    @PostRequest("/externalcontact/add_moment_task")
    WeResultDto addMomentTask(@JSONBody MomentsParamDto momentsParamDto);

}
