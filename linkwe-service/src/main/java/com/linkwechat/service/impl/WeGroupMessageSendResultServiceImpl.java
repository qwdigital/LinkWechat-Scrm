package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeGroupMessageSendResult;
import com.linkwechat.mapper.WeGroupMessageSendResultMapper;
import com.linkwechat.service.IWeGroupMessageSendResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 群发消息成员执行结果表(WeGroupMessageSendResult)
 *
 * @author danmo
 * @since 2022-04-06 22:29:04
 */
@Slf4j
@Service
public class WeGroupMessageSendResultServiceImpl extends ServiceImpl<WeGroupMessageSendResultMapper, WeGroupMessageSendResult> implements IWeGroupMessageSendResultService {

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
