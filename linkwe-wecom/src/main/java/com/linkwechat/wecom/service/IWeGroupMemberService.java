package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeGroupMember;
import com.linkwechat.wecom.domain.dto.WeGroupMemberDto;

import java.util.List;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/22 0022 0:02
 */
public interface IWeGroupMemberService extends IService<WeGroupMember> {

    List<WeGroupMember> selectWeGroupMemberList(WeGroupMember paramWeGroupMember);

    List<WeGroupMemberDto> selectWeGroupMemberListByChatId(String chatId);

    WeGroupMember selectWeGroupMemberByUnionId(String chatId, String unionId);

    void insertBatch(List<WeGroupMember> weGroupMembers);
}
