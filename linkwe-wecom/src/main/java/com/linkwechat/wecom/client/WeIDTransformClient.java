package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.agentdev.WeTransformExternalUserIdQuery;
import com.linkwechat.domain.wecom.query.agentdev.WeTransformUserIdQuery;
import com.linkwechat.domain.wecom.query.agentdev.WeUnionidTransformExternalUserIdQuery;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformCorpVO;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformExternalUserIdVO;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformUserIdVO;
import com.linkwechat.domain.wecom.vo.agentdev.WeUnionidTransformExternalUserIdVO;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.interceptor.WeProviderTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * @author leejoker
 * <p>
 * 用于自建应用id转换
 */
@BaseRequest(baseURL = "${weComServerUrl}")
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeIDTransformClient {

    /**
     * corpid的转换
     */
    @Post(url = "/service/corpid_to_opencorpid", interceptor = WeProviderTokenInterceptor.class)
    WeTransformCorpVO transformCorpId(@JSONBody WeBaseQuery query);

    /**
     * userid的转换
     */
    @Post(url = "/batch/userid_to_openuserid", interceptor = WeAccessTokenInterceptor.class)
    WeTransformUserIdVO transformUserId(@JSONBody WeTransformUserIdQuery query);

    /**
     * 代开发应用external_userid转换
     */
    @Request(url = "/externalcontact/to_service_external_userid", interceptor = WeAccessTokenInterceptor.class)
    WeTransformExternalUserIdVO transformExternalUserId(@JSONBody WeTransformExternalUserIdQuery query);


    /**
     * unionid转换为第三方external_userid
     *
     * @param query
     * @return {@link WeUnionidTransformExternalUserIdVO}
     * @author WangYX
     * @date 2022/10/26 16:34
     */
    @Post(value = "/idconvert/unionid_to_external_userid", interceptor = WeAccessTokenInterceptor.class)
    WeUnionidTransformExternalUserIdVO unionidTransformExternalUserId(@JSONBody WeUnionidTransformExternalUserIdQuery query);


}
