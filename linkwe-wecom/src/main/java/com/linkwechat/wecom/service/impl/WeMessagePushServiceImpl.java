package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.enums.PushType;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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
    private WeMessagePushClient weMessagePushClient;

    @Autowired
    private IWeGroupService weGroupService;

    @Override
    public WeMessagePush selectWeMessagePushById(Long messagePushId) {
        return weMessagePushMapper.selectById(messagePushId);
    }

    @Override
    public List<WeMessagePush> selectWeMessagePushList(WeMessagePush weMessagePush) {
        return weMessagePushMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public int insertWeMessagePush(WeMessagePush weMessagePush) {


        HashMap<String, Object> map = Maps.newHashMap();
        JSONObject jsonObject = new JSONObject(weMessagePush.getMessageJson());
        Optional<MessageType> of = MessageType.of(weMessagePush.getMessageType());
        of.ifPresent(messageType -> map.put(messageType.getMessageType(), jsonObject));

        if (weMessagePush.getPushType() != null && weMessagePush.getPushType().equals(PushType.SEND_TO_USER.getType())) {

            //发送消息
            WeMessagePushDto weMessagePushDto = new WeMessagePushDto();
            weMessagePushDto.setTouser(weMessagePush.getToUser());
            weMessagePushDto.setToparty(weMessagePush.getToParty());
            weMessagePushDto.setTotag(weMessagePush.getToTag());
            weMessagePushDto.setMsgtype(weMessagePush.getMessageType());

            //这个先写在配置文件中
            weMessagePushDto.setAgentid(1000003);
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

        }

        if (weMessagePush.getPushType() != null
                && weMessagePush.getPushType().equals(PushType.SENT_TO_USER_GROUP.getType())) {

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

        }

        return weMessagePushMapper.insert(weMessagePush);
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
