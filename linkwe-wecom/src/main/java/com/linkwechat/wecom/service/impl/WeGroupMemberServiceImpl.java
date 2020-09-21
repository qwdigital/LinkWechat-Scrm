package com.linkwechat.wecom.service.impl;

import com.linkwechat.wecom.domain.WeGroupMember;
import com.linkwechat.wecom.mapper.WeGroupMemberMapper;
import com.linkwechat.wecom.service.IWeGroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/22 0022 0:03
 */
@Service
public class WeGroupMemberServiceImpl implements IWeGroupMemberService {
    @Autowired
    private WeGroupMemberMapper weGroupMemberMapper;

    public WeGroupMember selectWeGroupMemberById(Long id) {
        return this.weGroupMemberMapper.selectWeGroupMemberById(id);
    }

    public List<WeGroupMember> selectWeGroupMemberList(WeGroupMember weGroupMember) {
        return this.weGroupMemberMapper.selectWeGroupMemberList(weGroupMember);
    }

    public int insertWeGroupMember(WeGroupMember weGroupMember) {
        return this.weGroupMemberMapper.insertWeGroupMember(weGroupMember);
    }

    public int updateWeGroupMember(WeGroupMember weGroupMember) {
        return this.weGroupMemberMapper.updateWeGroupMember(weGroupMember);
    }

    public int deleteWeGroupMemberByIds(Long[] ids) {
        return this.weGroupMemberMapper.deleteWeGroupMemberByIds(ids);
    }

    public int deleteWeGroupMemberById(Long id) {
        return this.weGroupMemberMapper.deleteWeGroupMemberById(id);
    }
}
