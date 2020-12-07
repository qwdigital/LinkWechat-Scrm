package com.linkwechat.wecom.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.linkwechat.common.enums.PushType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.wecom.domain.WeMessagePush;
import com.linkwechat.wecom.mapper.WeMessagePushMapper;
import com.linkwechat.wecom.service.IWeMessagePushService;
import com.linkwechat.wecom.strategy.MessageContext;
import com.linkwechat.wecom.strategy.SendMessageToUserGroupStrategy;
import com.linkwechat.wecom.strategy.SendMessageToUserStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 消息发送的Service接口
 *
 * @author KeWen
 * @date 2020-10-28
 */
@Service
public class WeMessagePushServiceImpl implements IWeMessagePushService {

    @Autowired
    private WeMessagePushMapper weMessagePushMapper;

    @Autowired
    private SendMessageToUserGroupStrategy sendMessageToUserGroupStrategy;

    @Autowired
    private SendMessageToUserStrategy sendMessageToUserStrategy;

    @Override
    public WeMessagePush selectWeMessagePushById(Long messagePushId) {
        return weMessagePushMapper.selectById(messagePushId);
    }

    @Override
    public List<WeMessagePush> selectWeMessagePushList(WeMessagePush weMessagePush) {
        return weMessagePushMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public void insertWeMessagePush(WeMessagePush weMessagePush) {

        sendMessgae(weMessagePush);

    }

    /**
     * 发送消息
     *
     * @param weMessagePush
     */
    public void sendMessgae(WeMessagePush weMessagePush) {
        if (weMessagePush.getPushType() != null && weMessagePush.getPushType().equals(PushType.SEND_TO_USER.getType())) {
            new MessageContext(sendMessageToUserStrategy).sendMessage(weMessagePush);
        }
        if (weMessagePush.getPushType() != null
                && weMessagePush.getPushType().equals(PushType.SENT_TO_USER_GROUP.getType())) {
            new MessageContext(sendMessageToUserGroupStrategy).sendMessage(weMessagePush);
        }
    }

    @Override
    public int deleteWeMessagePushByIds(Long[] messagePushIds) {
        return weMessagePushMapper.deleteBatchIds(Arrays.asList(messagePushIds));
    }

    @Override
    public int deleteWeMessagePushById(Long messagePushId) {
        return weMessagePushMapper.deleteById(messagePushId);
    }

}
