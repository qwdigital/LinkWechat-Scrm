package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeGroupMember;
import com.linkwechat.wecom.domain.dto.WeGroupMemberDto;
import com.linkwechat.wecom.mapper.WeGroupMemberMapper;
import com.linkwechat.wecom.service.IWeGroupMemberService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/22 0022 0:03
 */
@Service
public class WeGroupMemberServiceImpl extends ServiceImpl<WeGroupMemberMapper,WeGroupMember> implements IWeGroupMemberService {



    @Override
    public List<WeGroupMember> selectWeGroupMemberList(WeGroupMember weGroupMember) {
        return this.baseMapper.selectWeGroupMemberList(weGroupMember);
    }

    @Override
    public List<WeGroupMemberDto> selectWeGroupMemberListByChatId(String chatId) {
        return this.baseMapper.selectWeGroupMemberListByChatId(chatId);
    }

    @Override
    public WeGroupMember selectWeGroupMemberByUnionId(String chatId, String unionId) {
        return this.getOne(new LambdaQueryWrapper<WeGroupMember>()
        .eq(WeGroupMember::getChatId,chatId)
        .eq(WeGroupMember::getUnionId,unionId));
    }

    @Override
    public void insertBatch(List<WeGroupMember> weGroupMembers) {
        this.baseMapper.insertBatch(weGroupMembers);
    }


}
