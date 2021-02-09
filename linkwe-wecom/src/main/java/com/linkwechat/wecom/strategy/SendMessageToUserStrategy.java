package com.linkwechat.wecom.strategy;

import cn.hutool.json.JSONObject;
import com.google.common.collect.Maps;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.ReflectUtil;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.wecom.client.WeMessagePushClient;
import com.linkwechat.wecom.domain.WeMessagePush;
import com.linkwechat.wecom.domain.dto.WeMessagePushDto;
import com.linkwechat.wecom.domain.dto.WeMessagePushResultDto;
import com.linkwechat.wecom.mapper.WeMessagePushMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 发送应用消息
 */
@Service
public class SendMessageToUserStrategy implements Strategy {

    @Autowired
    private WeMessagePushMapper weMessagePushMapper;

    @Autowired
    private WeMessagePushClient weMessagePushClient;

    @Override
    public void sendMessage(WeMessagePush weMessagePush) {

        HashMap<String, Object> map = Maps.newHashMap();
        JSONObject jsonObject = new JSONObject(weMessagePush.getMessageJson());
        Optional<MessageType> of = MessageType.of(weMessagePush.getMessageType());
        of.ifPresent(messageType -> map.put(messageType.getMessageType(), jsonObject));
        //发送消息
        WeMessagePushDto weMessagePushDto = new WeMessagePushDto();
        Optional.ofNullable(weMessagePush.getToUser()).ifPresent(userId ->{
            String tousers = Arrays.stream(userId.split(",")).collect(Collectors.joining("|"));
            weMessagePushDto.setTouser(tousers);
        });
        Optional.ofNullable(weMessagePush.getToParty()).ifPresent(partyId ->{
            String toPartys = Arrays.stream(partyId.split(",")).collect(Collectors.joining("|"));
            weMessagePushDto.setToparty(toPartys);
        });
        Optional.ofNullable(weMessagePush.getToTag()).ifPresent(tagId ->{
            String tagIds = Arrays.stream(tagId.split(",")).collect(Collectors.joining("|"));
            weMessagePushDto.setTotag(tagIds);
        });
        weMessagePushDto.setMsgtype(weMessagePush.getMessageType());

        //这个先写在配置文件中
        weMessagePushDto.setAgentid(weMessagePush.getAgentid());
        weMessagePushDto.setSafe(0);
        weMessagePushDto.setEnable_id_trans(0);
        weMessagePushDto.setEnable_duplicate_check(0);
        weMessagePushDto.setDuplicate_check_interval(1800L);


        //动态添加微信消息体属性和属性值信息
        WeMessagePushDto target = (WeMessagePushDto) ReflectUtil.getTarget(weMessagePushDto, map);
        WeMessagePushResultDto weMessagePushResultDto = weMessagePushClient.sendMessageToUser(target,target.getAgentid().toString());

        if (weMessagePushResultDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)) {
            weMessagePush.setCreateTime(DateUtils.getNowDate());
            weMessagePush.setDelFlag(0);
            weMessagePush.setMessagePushId(SnowFlakeUtil.nextId());

            //存储返回结果信息
            weMessagePush.setInvaliduser(weMessagePushResultDto.getInvaliduser());
            weMessagePush.setInvalidparty(weMessagePushResultDto.getInvalidparty());
            weMessagePush.setInvalidtag(weMessagePushResultDto.getInvalidtag());
        }

        weMessagePushMapper.insert(weMessagePush);

    }


}
