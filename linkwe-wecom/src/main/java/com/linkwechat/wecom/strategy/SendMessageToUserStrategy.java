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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

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
        weMessagePushDto.setTouser(weMessagePush.getToUser());
        weMessagePushDto.setToparty(weMessagePush.getToParty());
        weMessagePushDto.setTotag(weMessagePush.getToTag());
        weMessagePushDto.setMsgtype(weMessagePush.getMessageType());

        //这个先写在配置文件中
        weMessagePushDto.setAgentid(weMessagePush.getAgentid());
        weMessagePushDto.setSafe(0);
        weMessagePushDto.setEnable_id_trans(0);
        weMessagePushDto.setEnable_duplicate_check(0);
        weMessagePushDto.setDuplicate_check_interval(1800L);


        //动态添加微信消息体属性和属性值信息
        WeMessagePushDto target = (WeMessagePushDto) ReflectUtil.getTarget(weMessagePushDto, map);
        WeMessagePushResultDto weMessagePushResultDto = weMessagePushClient.sendMessageToUser(target);

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
