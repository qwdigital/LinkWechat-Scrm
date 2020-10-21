package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeGroupMember;

import java.util.List;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/21 0021 23:58
 */
public interface WeGroupMemberMapper extends BaseMapper<WeGroupMember> {
//    WeGroupMember selectWeGroupMemberById(Long paramLong);

    List<WeGroupMember> selectWeGroupMemberList(WeGroupMember paramWeGroupMember);

//    int insertWeGroupMember(WeGroupMember paramWeGroupMember);
//
//    int updateWeGroupMember(WeGroupMember paramWeGroupMember);
//
//    int deleteWeGroupMemberById(Long paramLong);
//
//    int deleteWeGroupMemberByIds(Long[] paramArrayOfLong);
}
