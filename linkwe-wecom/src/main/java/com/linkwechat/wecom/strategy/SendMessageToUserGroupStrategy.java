package com.linkwechat.wecom.strategy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.ReflectUtil;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeMessagePushClient;
import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.domain.WeMessagePush;
import com.linkwechat.wecom.domain.dto.WeMessagePushGroupDto;
import com.linkwechat.wecom.domain.dto.WeMessagePushResultDto;
import com.linkwechat.wecom.mapper.WeMessagePushMapper;
import com.linkwechat.wecom.service.IWeGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * 应用推送消息
 */
@Service
public class SendMessageToUserGroupStrategy implements Strategy {

    @Autowired
    private WeMessagePushMapper weMessagePushMapper;

    @Autowired
    private WeMessagePushClient weMessagePushClient;

    @Autowired
    private IWeGroupService weGroupService;

    @Override
    public void sendMessage(WeMessagePush weMessagePush) {

        HashMap<String, Object> map = Maps.newHashMap();
        JSONObject jsonObject = new JSONObject(weMessagePush.getMessageJson());
        Optional<MessageType> of = MessageType.of(weMessagePush.getMessageType());
        of.ifPresent(messageType -> map.put(messageType.getMessageType(), jsonObject));
        //根据员工id列表查询所有的群信息
        List<String> strings = Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens(weMessagePush.getToUser(), WeConstans.COMMA));
        List<String> chatIds = Lists.newArrayList();
        strings.forEach(s -> {
            List<WeGroup> groups = weGroupService
                    .list(new LambdaQueryWrapper<WeGroup>().eq(WeGroup::getOwner, s));
            //发送消息到群聊
            if (CollectionUtil.isNotEmpty(groups)) {
                groups.forEach(d -> {
                    WeMessagePushGroupDto weMessagePushGroupDto = new WeMessagePushGroupDto();
                    weMessagePushGroupDto.setChatid(d.getChatId());
                    weMessagePushGroupDto.setMsgtype(weMessagePush.getMessageType());
                    weMessagePushGroupDto.setSafe(0);
                    //动态添加微信消息体属性和属性值信息
                    WeMessagePushGroupDto target = (WeMessagePushGroupDto) ReflectUtil.getTarget(weMessagePushGroupDto, map);
                    WeMessagePushResultDto weMessagePushResultDto = weMessagePushClient.sendMessageToUserGroup(target);

                    if (weMessagePushResultDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)) {
                        //保存发送的群消息
                        chatIds.add(d.getChatId());
                    }

                });
            }

        });

        weMessagePush.setCreateTime(DateUtils.getNowDate());
        weMessagePush.setDelFlag(0);
        weMessagePush.setMessagePushId(SnowFlakeUtil.nextId());
        weMessagePush.setChatId(CollectionUtil.isNotEmpty(chatIds) ? String.join(",", chatIds) : null);

        weMessagePushMapper.insert(weMessagePush);

    }


}
