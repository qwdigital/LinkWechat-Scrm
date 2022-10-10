package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.agentdev.WeTransformExternalUserIdQuery;
import com.linkwechat.domain.wecom.query.agentdev.WeTransformUserIdQuery;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformCorpVO;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformExternalUserIdVO;
import com.linkwechat.domain.wecom.vo.agentdev.WeTransformUserIdVO;
import com.linkwechat.fegin.QwCorpClient;
import com.linkwechat.fegin.QwTicketClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 企微企业接口回调
 * @date 2022/3/29 22:52
 **/
@Component
@Slf4j
public class QwTicketFallbackFactory implements QwTicketClient {

    @Override
    public AjaxResult<String> getCorpTicket(WeBaseQuery query) {
        return null;
    }

    @Override
    public AjaxResult<String> getAgentTicket(WeBaseQuery query) {
        return null;
    }

    @Override
    public AjaxResult<String> getWxTicket() {
        return null;
    }
}
