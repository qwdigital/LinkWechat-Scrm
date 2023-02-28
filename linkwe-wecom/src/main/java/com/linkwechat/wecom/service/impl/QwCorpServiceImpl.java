package com.linkwechat.wecom.service.impl;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.agentdev.WeTransformExternalUserIdQuery;
import com.linkwechat.domain.wecom.query.agentdev.WeTransformUserIdQuery;
import com.linkwechat.domain.wecom.query.agentdev.WeUnionidTransformExternalUserIdQuery;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformCorpVO;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformExternalUserIdVO;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformUserIdVO;
import com.linkwechat.domain.wecom.vo.agentdev.WeUnionidTransformExternalUserIdVO;
import com.linkwechat.wecom.client.WeIDTransformClient;
import com.linkwechat.wecom.service.IQwCorpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 微信token相关接口
 * @author: HaoN
 * @create: 2020-08-26 14:43
 **/
@Slf4j
@Service
public class QwCorpServiceImpl implements IQwCorpService {

    @Autowired
    private WeIDTransformClient weIDTransformClient;

    @Override
    public WeTransformCorpVO transformCorpId(WeBaseQuery query) {
        return weIDTransformClient.transformCorpId(query);
    }

    @Override
    public WeTransformUserIdVO transformUserId(WeTransformUserIdQuery query) {
        return weIDTransformClient.transformUserId(query);
    }

    @Override
    public WeTransformExternalUserIdVO transformExternalUserId(WeTransformExternalUserIdQuery query) {
        return weIDTransformClient.transformExternalUserId(query);
    }

    @Override
    public WeUnionidTransformExternalUserIdVO unionidTransformExternalUserId(WeUnionidTransformExternalUserIdQuery query) {
        return weIDTransformClient.unionidTransformExternalUserId(query);
    }
}
