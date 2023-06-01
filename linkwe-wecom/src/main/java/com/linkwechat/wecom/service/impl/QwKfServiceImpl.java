package com.linkwechat.wecom.service.impl;

import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.ReflectUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.kf.*;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.kf.*;
import com.linkwechat.wecom.client.WeKfClient;
import com.linkwechat.wecom.service.IQwKfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author danmo
 * @version 1.0
 * @date 2022/04/16 21:10
 */
@Service
@Slf4j
public class QwKfServiceImpl implements IQwKfService {

    @Resource
    private WeKfClient weKfClient;

    @Override
    public WeAddKfVo addAccount(WeKfAddQuery query) {
        return weKfClient.add(query);
    }

    @Override
    public WeResultVo delAccount(WeKfQuery query) {
        return weKfClient.delete(query);
    }

    @Override
    public WeResultVo updateAccount(WeKfQuery query) {
        return weKfClient.update(query);
    }

    @Override
    public WeKfListVo getAccountList(WeBaseQuery query) {
        QwKfListQuery weKfListQuery = new QwKfListQuery();
        weKfListQuery.setCorpid(query.getCorpid());
        return getAccountList(weKfListQuery);
    }

    private WeKfListVo getAccountList(QwKfListQuery weKfListQuery){
        WeKfListVo list = weKfClient.list(weKfListQuery);
        if(list != null && list.getAccountList().size() >= 100){
            weKfListQuery.setOffset(weKfListQuery.getLimit() + weKfListQuery.getOffset());
            WeKfListVo accountList = getAccountList(weKfListQuery);
            list.getAccountList().addAll(accountList.getAccountList());
        }
        return list;
    }

    @Override
    public WeKfDetailVo addContactWay(WeKfQuery query) {
        return weKfClient.addContactWay(query);
    }

    @Override
    public WeKfUserListVo addServicer(WeKfQuery query) {
        return weKfClient.addServicer(query);
    }

    @Override
    public WeKfUserListVo delServicer(WeKfQuery query) {
        return weKfClient.delServicer(query);
    }

    @Override
    public WeKfUserListVo getServicerList(WeKfQuery query) {
        return weKfClient.servicerList(query);
    }

    @Override
    public WeKfStateVo getSessionStatus(WeKfStateQuery query) {
        return weKfClient.getServiceState(query);
    }

    @Override
    public WeKfStateVo updateSessionStatus(WeKfStateQuery query) {
        return weKfClient.transServiceState(query);
    }

    @Override
    public WeKfMsgVo sendSessionMsg(WeKfMsgQuery query) {
        Map<String,Object> map = new HashMap<>(16);
        map.put(query.getMsgtype(),query.getContext());
        WeKfMsgQuery weKfMsgQuery = (WeKfMsgQuery) ReflectUtil.getTarget(query, map);
        weKfMsgQuery.setContext(null);
        return weKfClient.sendMsg(weKfMsgQuery);
    }

    @Override
    public WeKfGetMsgVo getSessionMsg(WeKfGetMsgQuery query) {
        WeKfGetMsgVo kfGetMsgVo = weKfClient.syncMsg(query);
        if (kfGetMsgVo != null && kfGetMsgVo.getHasMore().equals(1)) {
            query.setCursor(kfGetMsgVo.getNextCursor());
            WeKfGetMsgVo sessionMsg = getSessionMsg(query);
            kfGetMsgVo.getMsgList().addAll(sessionMsg.getMsgList());
        }
        return kfGetMsgVo;
    }

    @Override
    public WeCustomerInfoVo getCustomerInfos(WeKfCustomerQuery query) {
        return weKfClient.getKfCustomerBatch(query);
    }

    @Override
    public WeUpgradeServiceConfigVo getUpgradeServiceConfig(WeBaseQuery query) {
        return weKfClient.getUpgradeServiceConfig(query);
    }

    @Override
    public WeResultVo customerUpgradeService(WeUpgradeServiceQuery query) {
        return weKfClient.customerUpgradeService(query);
    }

    @Override
    public WeResultVo cancelUpgradeService(WeUpgradeServiceQuery query) {
        return weKfClient.customerCancelUpgradeService(query);
    }

    @Override
    public WeKfMsgVo sendMsgOnEvent(WeKfMsgQuery query) {
        if(query != null && StringUtils.isEmpty(query.getMsgtype())){
            throw new WeComException("消息类型不能为空");
        }

        if(query != null && StringUtils.isEmpty(query.getCode())){
            throw new WeComException("CODE不能为空");
        }

        if(query != null && StringUtils.isEmpty(query.getContext())){
            throw new WeComException("消息内容不能为空");
        }
        Map<String,Object> map = new HashMap<>(16);
        map.put(query.getMsgtype(),query.getContext());
        WeKfMsgQuery weKfMsgQuery = (WeKfMsgQuery) ReflectUtil.getTarget(query, map);
        weKfMsgQuery.setContext(null);
        return weKfClient.sendMsgOnEvent(weKfMsgQuery);
    }

    @Override
    public WeKfStatisticListVo getCorpStatistic(WeKfGetStatisticQuery query) {
        return weKfClient.getCorpStatistic(query);
    }

    @Override
    public WeKfStatisticListVo getServicerStatistic(WeKfGetStatisticQuery query) {
        return weKfClient.getServicerStatistic(query);
    }

    @Override
    public WeKfAddKnowledgeGroupVo addKnowledgeGroup(WeKfAddKnowledgeGroupQuery query) {
        return weKfClient.addKnowledgeGroup(query);
    }

    @Override
    public WeResultVo delKnowledgeGroup(WeKfKnowledgeGroupQuery query) {
        return weKfClient.delKnowledgeGroup(query);
    }

    @Override
    public WeResultVo modKnowledgeGroup(WeKfKnowledgeGroupQuery query) {
        return weKfClient.modKnowledgeGroup(query);
    }

    @Override
    public WeKfKnowledgeGroupListVo getKnowledgeGroupList(WeKfKnowledgeGroupQuery query) {
        return weKfClient.getKnowledgeGroupList(query);
    }
}
