package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.linkwechat.wecom.domain.dto.WeGroupMemberDto;
import com.linkwechat.wecom.domain.vo.WeChatContactMsgVo;
import com.linkwechat.wecom.service.IWeGroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.linkwechat.wecom.mapper.WeChatContactMsgMapper;
import com.linkwechat.wecom.domain.WeChatContactMsg;
import com.linkwechat.wecom.service.IWeChatContactMsgService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 会话消息Service业务层处理
 *
 * @author ruoyi
 * @date 2021-07-28
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
            weChatContactMsgVos.forEach(msg ->{
                List<WeGroupMemberDto> weGroupMemberDtos = weGroupMemberService.selectWeGroupMemberListByChatId(msg.getReceiver());
                if(CollectionUtil.isNotEmpty(weGroupMemberDtos)){
                    if(StringUtils.isEmpty(msg.getName())){
                        String names = weGroupMemberDtos.stream().map(WeGroupMemberDto::getMemberName)
                                .filter(StringUtils::isNotEmpty).collect(Collectors.joining(","));
                        msg.setName(names);
                    }
                    String avatars = weGroupMemberDtos.stream().map(WeGroupMemberDto::getMemberAvatar)
                            .filter(StringUtils::isNotEmpty).collect(Collectors.joining(","));
                    msg.setAvatar(avatars);
                }
            });
        }
        return weChatContactMsgVos;
    }

    @Override
    public List<WeChatContactMsgVo> selectFullSearchChatList(WeChatContactMsg weChatContactMsg) {
        return this.baseMapper.selectFullSearchChatList(weChatContactMsg);
    }
}
