package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeChatContactMsg;
import com.linkwechat.domain.WeGroupMember;
import com.linkwechat.domain.customer.vo.WeCustomerSimpleInfoVo;
import com.linkwechat.domain.groupchat.vo.WeGroupMemberVo;
import com.linkwechat.domain.msgaudit.query.WeChatContactMsgQuery;
import com.linkwechat.domain.msgaudit.vo.WeChatContactMsgVo;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeChatContactMsgMapper;
import com.linkwechat.service.IWeChatContactMsgService;
import com.linkwechat.service.IWeCustomerService;
import com.linkwechat.service.IWeGroupMemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
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

    @Autowired
    private IWeCustomerService weCustomerService;

    @Resource
    private QwSysUserClient qwSysUserClient;

    @Override
    public List<WeChatContactMsg> queryList(WeChatContactMsg weChatContactMsg) {
        LambdaQueryWrapper<WeChatContactMsg> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(weChatContactMsg.getMsgId())) {
            lqw.eq(WeChatContactMsg::getMsgId, weChatContactMsg.getMsgId());
        }
        if (StringUtils.isNotBlank(weChatContactMsg.getFromId())) {
            lqw.and(wrapper -> wrapper.eq(WeChatContactMsg::getFromId, weChatContactMsg.getFromId())
                    .or().eq(WeChatContactMsg::getToList, weChatContactMsg.getFromId()));
        }
        if (StringUtils.isNotBlank(weChatContactMsg.getToList())) {
            lqw.and(wrapper -> wrapper.eq(WeChatContactMsg::getFromId, weChatContactMsg.getToList())
                    .or().eq(WeChatContactMsg::getToList, weChatContactMsg.getToList()));
        }
        if (StringUtils.isNotBlank(weChatContactMsg.getRoomId())) {
            lqw.eq(WeChatContactMsg::getRoomId, weChatContactMsg.getRoomId());
        }
        if (StringUtils.isNotBlank(weChatContactMsg.getAction())) {
            lqw.eq(WeChatContactMsg::getAction, weChatContactMsg.getAction());
        }
        if (StringUtils.isNotBlank(weChatContactMsg.getMsgType())) {
            lqw.eq(WeChatContactMsg::getMsgType, weChatContactMsg.getMsgType());
        }
        if (weChatContactMsg.getMsgTime() != null) {
            lqw.and(wrapper -> wrapper.ge(WeChatContactMsg::getMsgTime, weChatContactMsg.getMsgTime())
                    .le(WeChatContactMsg::getMsgTime, weChatContactMsg.getMsgTime()));
        }
        if (StringUtils.isNotBlank(weChatContactMsg.getContact())) {
            lqw.like(WeChatContactMsg::getContact, weChatContactMsg.getContact());
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
        if (CollectionUtil.isNotEmpty(weChatContactMsgVos)) {
            List<String> roomIds = weChatContactMsgVos.stream().map(WeChatContactMsgVo::getReceiver).collect(Collectors.toList());
            List<WeGroupMemberVo> weGroupMemberList = weGroupMemberService.selectGroupMemberListByChatIds(roomIds);
            weChatContactMsgVos.forEach(msg -> {
                List<WeGroupMemberVo> currentGroupList = weGroupMemberList.stream().filter(member -> Objects.equals(msg.getReceiver(), member.getChatId())).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(currentGroupList)) {
                    if (StringUtils.isEmpty(msg.getName())) {
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

        List<WeChatContactMsgVo> weChatContactMsgVos = this.baseMapper.selectFullSearchChatList(query);
        if (CollectionUtil.isNotEmpty(weChatContactMsgVos)) {
            Map<String, SysUserVo> userIdMap = new HashMap<>(16);
            Map<String, String> userIdAndNameMap = new HashMap<>(16);
            Map<String, String> customerAndNameMap = new HashMap<>(16);
            //客户ID
            List<String> customerIds = weChatContactMsgVos.stream().filter(item -> item.getFromId().startsWith("wm") || item.getFromId().startsWith("wo")).map(WeChatContactMsgVo::getFromId).collect(Collectors.toList());

            if(CollectionUtil.isNotEmpty(customerIds)){
                List<WeCustomerSimpleInfoVo> customerSimpleInfos = weCustomerService.getCustomerSimpleInfo(customerIds);

                if(CollectionUtil.isNotEmpty(customerSimpleInfos)){
                    customerAndNameMap = customerSimpleInfos.stream().collect(Collectors.toMap(WeCustomerSimpleInfoVo::getExternalUserid, item -> item.getCustomerName() + "_" + item.getAvatar(), (key1, key2) -> key2));
                }

                List<WeGroupMember> groupMemberList = weGroupMemberService.list(new LambdaQueryWrapper<WeGroupMember>()
                        .select(WeGroupMember::getUserId,WeGroupMember::getName)
                        .in(WeGroupMember::getUserId,customerIds)
                        .eq(WeGroupMember::getType, 2)
                        .groupBy(WeGroupMember::getUserId,WeGroupMember::getName));
                if(CollectionUtil.isNotEmpty(groupMemberList)){
                    userIdAndNameMap = groupMemberList.stream().collect(Collectors.toMap(WeGroupMember::getUserId, WeGroupMember::getName, (key1, key2) -> key2));
                }
            }

            //成员ID
            List<String> userIds = weChatContactMsgVos.stream().filter(item -> !item.getFromId().startsWith("wm") && !item.getFromId().startsWith("wo")).map(WeChatContactMsgVo::getFromId).collect(Collectors.toList());

            if (CollectionUtil.isNotEmpty(userIds)) {
                SysUserQuery sysUserQuery = new SysUserQuery();
                sysUserQuery.setWeUserIds(userIds);
                List<SysUserVo> userList = qwSysUserClient.getUserListByWeUserIds(sysUserQuery).getData();
                if (CollectionUtil.isNotEmpty(userList)) {
                    userIdMap = userList.stream().collect(Collectors.toMap(SysUserVo::getWeUserId, Function.identity(), (key1, key2) -> key2));
                }
            }


            for (WeChatContactMsgVo msgVo : weChatContactMsgVos) {
                if (userIdMap.containsKey(msgVo.getFromId())) {
                    SysUserVo sysUserVo = userIdMap.get(msgVo.getFromId());
                    msgVo.setName(sysUserVo.getUserName());
                    msgVo.setAvatar(sysUserVo.getAvatar());
                }

                if (userIdAndNameMap.containsKey(msgVo.getFromId())) {
                    String name = userIdAndNameMap.get(msgVo.getFromId());
                    if(StringUtils.isNotEmpty(name)){
                        msgVo.setName(name);
                    }
                }
                if (customerAndNameMap.containsKey(msgVo.getFromId())) {
                    String nameAndAvatar = customerAndNameMap.get(msgVo.getFromId());
                    if(StringUtils.isNotEmpty(nameAndAvatar)){
                        String[] nameAndAvatarSplit = nameAndAvatar.split("_");
                        if(StringUtils.isNotEmpty(nameAndAvatarSplit[0])){
                            msgVo.setName(nameAndAvatarSplit[0]);
                        }
                        if(StringUtils.isNotEmpty(nameAndAvatarSplit[1])){
                            msgVo.setAvatar(nameAndAvatarSplit[1]);
                        }
                    }
                }
            }
        }
        return weChatContactMsgVos;
    }

    public static void main(String[] args) {

        String nameAndAvatar = "云店铺客服_https://wework.qpic.cn/wwpic/25283_-c3xxJaeSpa87Ji_1680091204/0";
        if(StringUtils.isNotEmpty(nameAndAvatar)){
            String[] nameAndAvatarSplit = nameAndAvatar.split("_");
            if(StringUtils.isNotEmpty(nameAndAvatarSplit[0])){
                System.out.println(nameAndAvatarSplit[0]);
            }
            if(StringUtils.isNotEmpty(nameAndAvatarSplit[1])){
                System.out.println(nameAndAvatarSplit[1]);
            }
        }
    }
}
