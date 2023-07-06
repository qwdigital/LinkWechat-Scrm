package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeChatContactMsg;
import com.linkwechat.domain.groupchat.vo.WeGroupMemberVo;
import com.linkwechat.domain.msgaudit.query.WeChatContactMsgQuery;
import com.linkwechat.domain.msgaudit.vo.WeChatContactMsgVo;
import com.linkwechat.mapper.WeChatContactMsgMapper;
import com.linkwechat.service.IWeChatContactMsgService;
import com.linkwechat.service.IWeGroupMemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 会话消息(WeChatContactMsg)
 *
 * @author danmo
 * @since 2022-05-06 11:54:51
 */
@Service
public class WeChatContactMsgServiceImpl extends ServiceImpl<WeChatContactMsgMapper, WeChatContactMsg> implements IWeChatContactMsgService {

    @Autowired
    private IWeGroupMemberService weGroupMemberService;

    @Override
    public List<WeChatContactMsg> queryList(WeChatContactMsg weChatContactMsg) {
        LambdaQueryWrapper<WeChatContactMsg> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(weChatContactMsg.getMsgId())){
            lqw.eq(WeChatContactMsg::getMsgId ,weChatContactMsg.getMsgId());
        }
        if (StringUtils.isNotBlank(weChatContactMsg.getFromId())){
            lqw.and(wrapper -> wrapper.eq(WeChatContactMsg::getFromId ,weChatContactMsg.getFromId())
                    .or().eq(WeChatContactMsg::getToList,weChatContactMsg.getFromId()));
        }
        if (StringUtils.isNotBlank(weChatContactMsg.getToList())){
            lqw.and(wrapper -> wrapper.eq(WeChatContactMsg::getFromId ,weChatContactMsg.getToList())
                    .or().eq(WeChatContactMsg::getToList,weChatContactMsg.getToList()));
        }
        if (StringUtils.isNotBlank(weChatContactMsg.getRoomId())){
            lqw.eq(WeChatContactMsg::getRoomId ,weChatContactMsg.getRoomId());
        }
        if (StringUtils.isNotBlank(weChatContactMsg.getAction())){
            lqw.eq(WeChatContactMsg::getAction ,weChatContactMsg.getAction());
        }
        if (StringUtils.isNotBlank(weChatContactMsg.getMsgType())){
            lqw.eq(WeChatContactMsg::getMsgType ,weChatContactMsg.getMsgType());
        }
        if (weChatContactMsg.getMsgTime() != null){
            lqw.and(wrapper -> wrapper.ge(WeChatContactMsg::getMsgTime,weChatContactMsg.getMsgTime())
                    .le(WeChatContactMsg::getMsgTime,weChatContactMsg.getMsgTime()));
        }
        if (StringUtils.isNotBlank(weChatContactMsg.getContact())){
            lqw.like(WeChatContactMsg::getContact ,weChatContactMsg.getContact());
        }
        lqw.orderByDesc(WeChatContactMsg::getMsgTime);
        return this.list(lqw);
    }

    @Override
    public List<WeChatContactMsgVo> selectExternalChatList(String fromId) {
        return this.baseMapper.selectExternalChatList(fromId);
    }

    @Override
    public List<WeChatContactMsgVo> selectAloneChatList(WeChatContactMsg weChatContactMsg) {
        return this.baseMapper.selectAloneChatList(weChatContactMsg);
    }

    @Override
    public List<WeChatContactMsgVo> selectInternalChatList(String fromId) {
        return this.baseMapper.selectInternalChatList(fromId);
    }

    @Override
    public List<WeChatContactMsgVo> selectGroupChatList(String fromId) {
        List<WeChatContactMsgVo> weChatContactMsgVos = this.baseMapper.selectGroupChatList(fromId);
        if(CollectionUtil.isNotEmpty(weChatContactMsgVos)){
            List<String> roomIds = weChatContactMsgVos.stream().map(WeChatContactMsgVo::getReceiver).collect(Collectors.toList());
            List<WeGroupMemberVo> weGroupMemberList = weGroupMemberService.selectGroupMemberListByChatIds(roomIds);
            weChatContactMsgVos.forEach(msg ->{
                List<WeGroupMemberVo> currentGroupList = weGroupMemberList.stream().filter(member -> Objects.equals(msg.getReceiver(), member.getChatId())).collect(Collectors.toList());
                if(CollectionUtil.isNotEmpty(currentGroupList)){
                    if(StringUtils.isEmpty(msg.getName())){
                        String names = currentGroupList.stream().map(WeGroupMemberVo::getName)
                                .filter(StringUtils::isNotEmpty).collect(Collectors.joining(","));
                        msg.setName(names);
                    }
                    String avatars = currentGroupList.stream().map(WeGroupMemberVo::getAvatar)
                            .filter(StringUtils::isNotEmpty).collect(Collectors.joining(","));
                    msg.setAvatar(avatars);
                }
            });
        }
        return weChatContactMsgVos;
    }

    @Override
    public List<WeChatContactMsgVo> selectFullSearchChatList(WeChatContactMsgQuery query) {
        return this.baseMapper.selectFullSearchChatList(query);
    }
}
