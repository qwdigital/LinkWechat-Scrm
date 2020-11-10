package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.enums.PushType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.ReflectUtil;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeMessagePushClient;
import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.domain.WeMessagePush;
import com.linkwechat.wecom.domain.dto.WeMessagePushDto;
import com.linkwechat.wecom.domain.dto.WeMessagePushGroupDto;
import com.linkwechat.wecom.domain.dto.WeMessagePushResultDto;
import com.linkwechat.wecom.mapper.WeMessagePushMapper;
import com.linkwechat.wecom.service.IWeGroupService;
import com.linkwechat.wecom.service.IWeMessagePushService;
import com.linkwechat.wecom.strategy.MessageContext;
import com.linkwechat.wecom.strategy.SendMessageToUserGroupStrategy;
import com.linkwechat.wecom.strategy.SendMessageToUserStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

        //立即发送
        if (null == weMessagePush.getSettingTime()) {
            sendMessgae(weMessagePush);
        } else {

            //发送时间不能小于当前时间
            //定时发送消息(异步执行)
            //保存消息体到数据库，增加一个发送状态(0 已发送 1 未发送)
            //定义一个任务执行队列
            //把任务放入执行队列
            //任务执行 sendMessgae(WeMessagePush weMessagePush) 方法
            if (DateUtils.diffTime(new Date(), DateUtil.parse(weMessagePush.getSettingTime(), "yyyy-MM-dd HH:mm:ss")) < 0) {
                throw new WeComException("发送时间不能小于当前时间");
            }

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    sendMessgae(weMessagePush);
                }
            }, DateUtil.parse(weMessagePush.getSettingTime(), "yyyy-MM-dd HH:mm:ss"));

        }

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
