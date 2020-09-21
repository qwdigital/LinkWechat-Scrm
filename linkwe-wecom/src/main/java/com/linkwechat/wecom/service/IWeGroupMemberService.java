package com.linkwechat.wecom.service;

import com.linkwechat.wecom.domain.WeGroupMember;

import java.util.List;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/22 0022 0:02
 */
public interface IWeGroupMemberService {
    WeGroupMember selectWeGroupMemberById(Long paramLong);

    List<WeGroupMember> selectWeGroupMemberList(WeGroupMember paramWeGroupMember);

    int insertWeGroupMember(WeGroupMember paramWeGroupMember);

    int updateWeGroupMember(WeGroupMember paramWeGroupMember);

    int deleteWeGroupMemberByIds(Long[] paramArrayOfLong);

    int deleteWeGroupMemberById(Long paramLong);
}
