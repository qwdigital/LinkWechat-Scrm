package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.enums.QwAppMsgBusinessTypeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeAgentMsg;
import com.linkwechat.domain.agent.query.WeAgentMsgAddQuery;
import com.linkwechat.domain.agent.query.WeAgentMsgListQuery;
import com.linkwechat.domain.agent.vo.WeAgentMsgListVo;
import com.linkwechat.domain.agent.vo.WeAgentMsgVo;
import com.linkwechat.domain.msg.QwAppMsgBody;
import com.linkwechat.domain.wecom.query.msg.WeRecallMsgQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.fegin.QwAppMsgClient;
import com.linkwechat.mapper.WeAgentMsgMapper;
import com.linkwechat.service.IWeAgentMsgService;
import com.linkwechat.service.QwAppSendMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 应用消息表(WeAgentMsg)
 *
 * @author danmo
 * @since 2022-11-04 17:08:08
 */
@Service
public class WeAgentMsgServiceImpl extends ServiceImpl<WeAgentMsgMapper, WeAgentMsg> implements IWeAgentMsgService {

    @Autowired
    private QwAppSendMsgService qwAppSendMsgService;


    @Resource
    private QwAppMsgClient qwAppMsgClient;

    @Override
    public void addMsg(WeAgentMsgAddQuery query) {
        WeAgentMsg agentMsg = new WeAgentMsg();
        agentMsg.setStatus(query.getStatus());
        agentMsg.setAgentId(query.getAgentId());
        agentMsg.setMsgTitle(query.getMsgTitle());
        agentMsg.setScopeType(query.getScopeType());
        agentMsg.setSendType(query.getSendType());
        agentMsg.setPlanSendTime(query.getPlanSendTime());
        if(CollectionUtil.isNotEmpty(query.getToUser())){
            agentMsg.setToUser(String.join(",", query.getToUser()));
        }

        if(CollectionUtil.isNotEmpty(query.getToParty())){
            agentMsg.setToParty(String.join(",", query.getToParty()));
        }
        if(CollectionUtil.isNotEmpty(query.getToTag())){
            agentMsg.setToTag(String.join(",", query.getToTag()));
        }
        agentMsg.setMsgType(query.getWeMessageTemplate().getMsgType());
        agentMsg.setTitle(query.getWeMessageTemplate().getTitle());
        agentMsg.setDescription(query.getWeMessageTemplate().getDescription());
        agentMsg.setFileUrl(query.getWeMessageTemplate().getFileUrl());
        agentMsg.setLinkUrl(query.getWeMessageTemplate().getLinkUrl());
        agentMsg.setContent(query.getWeMessageTemplate().getContent());
        agentMsg.setPicUrl(query.getWeMessageTemplate().getPicUrl());
        agentMsg.setAppId(query.getWeMessageTemplate().getAppId());
        if (save(agentMsg) && Objects.equals(1, query.getStatus()) && Objects.equals(1, query.getSendType())) {
            QwAppMsgBody body = new QwAppMsgBody();
            body.setMessageTemplates(query.getWeMessageTemplate());
            body.setBusinessType(QwAppMsgBusinessTypeEnum.AGENT.getType());
            body.setCorpId(SecurityUtils.getCorpId());
            body.setCorpUserIds(query.getToUser());
            body.setDeptIds(query.getToParty());
            body.setTagIds(query.getToTag());
            body.setCorpUserIds(query.getToUser());
            body.setCallBackId(agentMsg.getId());
            qwAppSendMsgService.appMsgSend(body);
        }
    }

    @Override
    public void updateMsg(WeAgentMsgAddQuery query) {
        LambdaUpdateWrapper<WeAgentMsg> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(StringUtils.isNotEmpty(query.getMsgTitle()), WeAgentMsg::getTitle, query.getMsgTitle());
        wrapper.set(Objects.nonNull(query.getScopeType()), WeAgentMsg::getScopeType, query.getScopeType());
        wrapper.set(Objects.nonNull(query.getSendType()), WeAgentMsg::getSendType, query.getSendType());
        wrapper.set(Objects.nonNull(query.getPlanSendTime()), WeAgentMsg::getPlanSendTime, query.getPlanSendTime());
        wrapper.set(CollectionUtil.isNotEmpty(query.getToUser()), WeAgentMsg::getToUser, String.join(",", query.getToUser()));
        wrapper.set(CollectionUtil.isNotEmpty(query.getToParty()), WeAgentMsg::getToParty, String.join(",", query.getToParty()));
        //wrapper.set(CollectionUtil.isNotEmpty(query.getToTag()), WeAgentMsg::getToTag, String.join(",", query.getToTag()));
        wrapper.set(StringUtils.isNotEmpty(query.getWeMessageTemplate().getMsgType()), WeAgentMsg::getMsgType, query.getWeMessageTemplate().getMsgType());
        wrapper.set(StringUtils.isNotEmpty(query.getWeMessageTemplate().getDescription()), WeAgentMsg::getDescription, query.getWeMessageTemplate().getDescription());
        wrapper.set(StringUtils.isNotEmpty(query.getWeMessageTemplate().getTitle()), WeAgentMsg::getTitle, query.getWeMessageTemplate().getTitle());
        wrapper.set(StringUtils.isNotEmpty(query.getWeMessageTemplate().getFileUrl()), WeAgentMsg::getFileUrl, query.getWeMessageTemplate().getFileUrl());
        wrapper.set(StringUtils.isNotEmpty(query.getWeMessageTemplate().getLinkUrl()), WeAgentMsg::getLinkUrl, query.getWeMessageTemplate().getLinkUrl());
        wrapper.set(StringUtils.isNotEmpty(query.getWeMessageTemplate().getContent()), WeAgentMsg::getContent, query.getWeMessageTemplate().getContent());
        wrapper.set(StringUtils.isNotEmpty(query.getWeMessageTemplate().getPicUrl()), WeAgentMsg::getPicUrl, query.getWeMessageTemplate().getPicUrl());
        wrapper.set(StringUtils.isNotEmpty(query.getWeMessageTemplate().getPicUrl()), WeAgentMsg::getPicUrl, query.getWeMessageTemplate().getPicUrl());
        wrapper.set(StringUtils.isNotEmpty(query.getWeMessageTemplate().getAppId()), WeAgentMsg::getAppId, query.getWeMessageTemplate().getAppId());
        update(wrapper.eq(WeAgentMsg::getId, query.getId()));
        if (Objects.equals(1, query.getStatus()) && Objects.equals(1, query.getSendType())) {
            QwAppMsgBody body = new QwAppMsgBody();
            body.setMessageTemplates(query.getWeMessageTemplate());
            body.setBusinessType(QwAppMsgBusinessTypeEnum.AGENT.getType());
            body.setCorpId(SecurityUtils.getCorpId());
            body.setCorpUserIds(query.getToUser());
            body.setDeptIds(query.getToParty());
            body.setTagIds(query.getToParty());
            body.setCorpUserIds(query.getToUser());
            body.setCallBackId(query.getId());
            qwAppSendMsgService.appMsgSend(body);
        }
    }

    @Override
    public void deleteMsg(Long id) {
        WeAgentMsg agentMsg = new WeAgentMsg();
        agentMsg.setId(id);
        agentMsg.setDelFlag(1);
        updateById(agentMsg);
    }

    @Override
    public WeAgentMsgVo getMsgInfo(Long id) {
        return this.baseMapper.getMsgInfo(id);
    }

    @Override
    public void revokeMsgInfo(Long id) {
        WeAgentMsg agentMsg = getById(id);
        if (Objects.isNull(agentMsg)) {
            throw new WeComException("无效ID!");
        }
        if (StringUtils.isEmpty(agentMsg.getMsgId())) {
            throw new WeComException("无有效消息ID!");
        }
        WeRecallMsgQuery query = new WeRecallMsgQuery();
        query.setAgentid(String.valueOf(agentMsg.getAgentId()));
        query.setCorpid(SecurityUtils.getCorpId());
        query.setMsgid(agentMsg.getMsgId());
        WeResultVo weResult = qwAppMsgClient.recallAgentAppMsg(query).getData();
        if (Objects.nonNull(weResult) && Objects.equals(0, weResult.getErrCode())) {
            update(new LambdaUpdateWrapper<WeAgentMsg>().set(WeAgentMsg::getStatus, 4).eq(WeAgentMsg::getId, id));
        } else {
            throw new WeComException(weResult.getErrCode(), "消息撤回失败");
        }
    }

    @Override
    public List<WeAgentMsg> getWaitingList() {
        LambdaQueryWrapper<WeAgentMsg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WeAgentMsg::getStatus, 1)
                .eq(WeAgentMsg::getSendType, 2)
                .lt(WeAgentMsg::getPlanSendTime, new Date())
                .eq(WeAgentMsg::getDelFlag, 0);
       return list(wrapper);
    }

    @Override
    public List<WeAgentMsgListVo> getMsgList(WeAgentMsgListQuery query) {
        if(Objects.isNull(query.getAgentId())){
            throw new WeComException("应用ID不能为空");
        }
        return this.baseMapper.getMsgList(query);
    }

}