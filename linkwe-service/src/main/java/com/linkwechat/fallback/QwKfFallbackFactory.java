package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.kf.*;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.kf.*;
import com.linkwechat.fegin.QwKfClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @date 2022/04/16 23:00
 */
@Component
@Slf4j
public class QwKfFallbackFactory implements QwKfClient {

    @Override
    public AjaxResult<WeAddKfVo> addAccount(WeKfAddQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> delAccount(WeKfQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> updateAccount(WeKfQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeKfListVo> getAccountList(WeBaseQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeKfDetailVo> addContactWay(WeKfQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeKfUserListVo> addServicer(WeKfQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeKfUserListVo> delServicer(WeKfQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeKfUserListVo> getServicerList(WeKfQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeKfStateVo> getSessionStatus(WeKfStateQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeKfStateVo> updateSessionStatus(WeKfStateQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeKfMsgVo> sendSessionMsg(WeKfMsgQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeKfGetMsgVo> getSessionMsg(WeKfGetMsgQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeCustomerInfoVo> getCustomerInfos(WeKfCustomerQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeUpgradeServiceConfigVo> getUpgradeServiceConfig(WeBaseQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> customerUpgradeService(WeUpgradeServiceQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> cancelUpgradeService(WeUpgradeServiceQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeKfMsgVo> sendMsgOnEvent(WeKfMsgQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeKfStatisticListVo> getCorpStatistic(WeKfGetStatisticQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeKfStatisticListVo> getServicerStatistic(WeKfGetStatisticQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeKfAddKnowledgeGroupVo> addKnowledgeGroup(WeKfAddKnowledgeGroupQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> delKnowledgeGroup(WeKfKnowledgeGroupQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> modKnowledgeGroup(WeKfKnowledgeGroupQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeKfKnowledgeGroupListVo> getKnowledgeGroupList(WeKfKnowledgeGroupQuery query) {
        return null;
    }
}
