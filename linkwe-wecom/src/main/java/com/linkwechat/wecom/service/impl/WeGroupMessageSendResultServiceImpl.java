package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeCustomerMessagePushClient;
import com.linkwechat.wecom.domain.WeGroupMessageSendResult;
import com.linkwechat.wecom.domain.WeGroupMessageTask;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgListDto;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgSendDto;
import com.linkwechat.wecom.domain.query.WeGetGroupMsgListQuery;
import com.linkwechat.wecom.mapper.WeGroupMessageSendResultMapper;
import com.linkwechat.wecom.service.IWeGroupMessageSendResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 群发消息成员执行结果Service业务层处理
 *
 * @author ruoyi
 * @date 2021-10-19
 */
@Service
public class WeGroupMessageSendResultServiceImpl extends ServiceImpl<WeGroupMessageSendResultMapper, WeGroupMessageSendResult> implements IWeGroupMessageSendResultService {
    @Autowired
    private WeCustomerMessagePushClient messagePushClient;

    @Override
    public List<WeGroupMessageSendResult> queryList(WeGroupMessageSendResult weGroupMessageSendResult) {
        LambdaQueryWrapper<WeGroupMessageSendResult> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(weGroupMessageSendResult.getMsgId())) {
            lqw.eq(WeGroupMessageSendResult::getMsgId, weGroupMessageSendResult.getMsgId());
        }
        if (StringUtils.isNotBlank(weGroupMessageSendResult.getUserId())) {
            lqw.eq(WeGroupMessageSendResult::getUserId, weGroupMessageSendResult.getUserId());
        }
        if (StringUtils.isNotBlank(weGroupMessageSendResult.getExternalUserid())) {
            lqw.eq(WeGroupMessageSendResult::getExternalUserid, weGroupMessageSendResult.getExternalUserid());
        }
        if (StringUtils.isNotBlank(weGroupMessageSendResult.getChatId())) {
            lqw.eq(WeGroupMessageSendResult::getChatId, weGroupMessageSendResult.getChatId());
        }
        if (weGroupMessageSendResult.getStatus() != null) {
            lqw.eq(WeGroupMessageSendResult::getStatus, weGroupMessageSendResult.getStatus());
        }
        if (weGroupMessageSendResult.getSendTime() != null) {
            lqw.eq(WeGroupMessageSendResult::getSendTime, weGroupMessageSendResult.getSendTime());
        }
        return this.list(lqw);
    }

    @Override
    public WeGroupMsgListDto getGroupMsgSendResult(String msgId, String userId, String cursor) {
        WeGetGroupMsgListQuery query = new WeGetGroupMsgListQuery();
        query.setMsgid(msgId);
        query.setUserid(userId);
        query.setCursor(cursor);
        WeGroupMsgListDto groupMsgSendResult = messagePushClient.getGroupMsgSendResult(query);
        if (groupMsgSendResult != null && ObjectUtil.equal(0, groupMsgSendResult.getErrcode())) {
            if (StringUtils.isNotEmpty(groupMsgSendResult.getNextCursor())) {
                WeGroupMsgListDto clildMsgList = getGroupMsgSendResult(msgId, userId, groupMsgSendResult.getNextCursor());
                List<WeGroupMsgSendDto> sendList = clildMsgList.getSendList();
                sendList.addAll(clildMsgList.getSendList());
                groupMsgSendResult.setSendList(sendList);
                groupMsgSendResult.setNextCursor(clildMsgList.getNextCursor());
            }
        }
        return groupMsgSendResult;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addOrUpdateBatchByCondition(List<WeGroupMessageSendResult> sendResultList) {
        if (CollectionUtil.isNotEmpty(sendResultList)) {
            sendResultList.forEach(item -> {
                LambdaQueryWrapper<WeGroupMessageSendResult> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(WeGroupMessageSendResult::getUserId, item.getUserId());
                if(item.getMsgTemplateId() != null){
                    wrapper.eq(WeGroupMessageSendResult::getMsgTemplateId,item.getMsgTemplateId());
                }
                if(item.getExternalUserid() != null){
                    wrapper.eq(WeGroupMessageSendResult::getExternalUserid,item.getExternalUserid());
                }
                if(item.getChatId() != null){
                    wrapper.eq(WeGroupMessageSendResult::getChatId,item.getChatId());
                }
                if (!this.update(item, wrapper)) {
                    this.save(item);
                }
            });
        }
    }

    @Override
    public List<WeGroupMessageSendResult> groupMsgSendResultList(WeGroupMessageSendResult sendResult) {
        return this.baseMapper.groupMsgSendResultList(sendResult);
    }
}
