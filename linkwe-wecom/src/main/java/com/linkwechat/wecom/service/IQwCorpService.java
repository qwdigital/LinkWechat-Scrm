package com.linkwechat.wecom.service;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.agentdev.WeTransformExternalUserIdQuery;
import com.linkwechat.domain.wecom.query.agentdev.WeTransformUserIdQuery;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformCorpVO;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformExternalUserIdVO;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformUserIdVO;

/**
 * @author danmo
 * @Description 企业业务接口
 * @date 2021/12/16 17:12
 **/
public interface IQwCorpService {

    /**
     * corpid的转换
     * @param query
     * @return
     */
    WeTransformCorpVO transformCorpId(WeBaseQuery query);

    /**
     * userid转化
     * @param query
     * @return
     */
    WeTransformUserIdVO transformUserId(WeTransformUserIdQuery query);

    /**
     * eid转化
     * @param query
     * @return
     */
    WeTransformExternalUserIdVO transformExternalUserId(WeTransformExternalUserIdQuery query);
}
