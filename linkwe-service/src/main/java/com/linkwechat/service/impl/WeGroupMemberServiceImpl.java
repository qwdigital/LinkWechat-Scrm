package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeGroupMember;
import com.linkwechat.domain.groupchat.vo.WeGroupChannelCountVo;
import com.linkwechat.domain.groupchat.vo.WeGroupMemberVo;
import com.linkwechat.mapper.WeGroupMemberMapper;
import com.linkwechat.service.IWeGroupMemberService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 企业微信群成员(WeGroupMember)
 *
 * @author danmo
 * @since 2022-04-02 13:35:14
 */
@Service
public class WeGroupMemberServiceImpl extends ServiceImpl<WeGroupMemberMapper, WeGroupMember> implements IWeGroupMemberService {


    @Override
    public List<WeGroupMember> getPageList(WeGroupMember weGroupMember) {
        return this.baseMapper.getPageList(weGroupMember);
    }

    @Override
    public void insertBatch(List<WeGroupMember> weGroupMembers) {
        this.baseMapper.insertBatch(weGroupMembers);
    }

    @Override
    public List<WeGroupMemberVo> selectGroupMemberListByChatId(String chatId) {
        return this.baseMapper.selectGroupMemberListByChatId(chatId);
    }

    @Override
    public List<WeGroupMemberVo> selectGroupMemberListByChatIds(List<String> chatIds) {
        return this.baseMapper.selectGroupMemberListByChatIds(chatIds);
    }

    @Override
    public void quitGroup(Integer quitScene,String userId, String chatId) {
        this.baseMapper.quitGroup(quitScene,userId,chatId);
    }

    @Override
    public void physicalDelete(String chatId, String userId) {
        this.baseMapper.physicalDelete(chatId,userId);
    }

    @Override
    public List<WeGroupChannelCountVo> getMemberNumByState(String state, Date startTime, Date endTime) {
        return this.baseMapper.getMemberNumByState(state,startTime,endTime);
    }
}
