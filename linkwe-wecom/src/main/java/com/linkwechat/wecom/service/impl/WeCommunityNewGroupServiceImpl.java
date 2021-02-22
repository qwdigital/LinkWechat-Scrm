package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.wecom.domain.WeCommunityNewGroup;
import com.linkwechat.wecom.domain.WeEmpleCode;
import com.linkwechat.wecom.domain.WeGroupCode;
import com.linkwechat.wecom.domain.dto.WeCommunityNewGroupDto;
import com.linkwechat.wecom.domain.vo.WeCommunityNewGroupVo;
import com.linkwechat.wecom.mapper.WeCommunityNewGroupMapper;
import com.linkwechat.wecom.mapper.WeGroupCodeMapper;
import com.linkwechat.wecom.service.IWeCommunityNewGroupService;
import com.linkwechat.wecom.service.IWeEmpleCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 社群运营 新客自动拉群
 *
 * @author kewen
 * @date 2021-02-19
 */
@Service
@Slf4j
public class WeCommunityNewGroupServiceImpl  extends ServiceImpl<WeCommunityNewGroupMapper, WeCommunityNewGroup> implements IWeCommunityNewGroupService {

    @Autowired
    private WeGroupCodeMapper weGroupCodeMapper;

    @Autowired
    private IWeEmpleCodeService weEmpleCodeService;

    @Autowired
    private WeCommunityNewGroupMapper weCommunityNewGroupMapper;

    @Override
    public int add(WeCommunityNewGroupDto communityNewGroupDto) {

        //检查群活码是否存在
        WeGroupCode weGroupCode = weGroupCodeMapper.selectWeGroupCodeById(communityNewGroupDto.getGroupCodeId());
        if(null!=weGroupCode){
            throw new WeComException("群活码不存在！");
        }
        WeEmpleCode weEmpleCode=new WeEmpleCode();
        weEmpleCode.setCodeType(2);
        weEmpleCode.setActivityScene(communityNewGroupDto.getActivityScene());
        weEmpleCode.setIsJoinConfirmFriends(communityNewGroupDto.getIsJoinConfirmFriends());
        weEmpleCode.setWelcomeMsg(communityNewGroupDto.getWelcomeMsg());
        weEmpleCode.setWeEmpleCodeUseScops(communityNewGroupDto.getWeEmpleCodeUseScops());
        weEmpleCode.setWeEmpleCodeTags(communityNewGroupDto.getWeEmpleCodeTags());
        weEmpleCode.setCreateTime(new Date());
        weEmpleCode.setCreateBy(SecurityUtils.getUsername());
        //生成员工活码信息
        weEmpleCodeService.insertWeEmpleCode(weEmpleCode);
        //保存新客自动拉群信息
        WeCommunityNewGroup communityNewGroup=new WeCommunityNewGroup();
        communityNewGroup.setNewGroupId(weGroupCode.getId());
        communityNewGroup.setEmpleCodeId(weEmpleCode.getId());
        communityNewGroup.setEmpleCodeName(communityNewGroupDto.getActivityScene());
        communityNewGroup.setDelFlag(0);
        communityNewGroup.setCreateTime(new Date());
        communityNewGroup.setCreateBy(SecurityUtils.getUsername());
        this.save(communityNewGroup);

        return 1;

    }

    @Override
    public List<WeCommunityNewGroupVo> selectWeCommunityNewGroupList(WeCommunityNewGroup communityNewGroup) {
        return weCommunityNewGroupMapper.selectWeCommunityNewGroupList(communityNewGroup);
    }

    @Override
    public WeCommunityNewGroupVo selectWeCommunityNewGroupById(Long newGroupId) {
        return weCommunityNewGroupMapper.selectWeCommunityNewGroupById(newGroupId);
    }

    @Override
    public int updateWeCommunityNewGroup(WeCommunityNewGroupDto communityNewGroupDto) {

        //检查群活码是否存在
        WeGroupCode weGroupCode = weGroupCodeMapper.selectWeGroupCodeById(communityNewGroupDto.getGroupCodeId());
        if(null!=weGroupCode){
            throw new WeComException("群活码不存在！");
        }

        //查询新客自动拉群信息
        WeCommunityNewGroup communityNewGroup = weCommunityNewGroupMapper.selectById(communityNewGroupDto.getGroupCodeId());
        if(null!=communityNewGroup){
            throw new WeComException("信息不存在！");
        }

        //查询员工活码信息
        WeEmpleCode weEmpleCode = weEmpleCodeService.selectWeEmpleCodeById(communityNewGroupDto.getWeEmpleCodeId());
        if(null!=weEmpleCode){
            throw new WeComException("员工活码信息不存在！");
        }

        //更新员工活码信息
        weEmpleCode.setActivityScene(communityNewGroupDto.getActivityScene());
        weEmpleCode.setIsJoinConfirmFriends(communityNewGroupDto.getIsJoinConfirmFriends());
        weEmpleCode.setWelcomeMsg(communityNewGroupDto.getWelcomeMsg());
        weEmpleCode.setWeEmpleCodeUseScops(communityNewGroupDto.getWeEmpleCodeUseScops());
        weEmpleCode.setWeEmpleCodeTags(communityNewGroupDto.getWeEmpleCodeTags());
        weEmpleCode.setCreateBy(SecurityUtils.getUsername());
        weEmpleCodeService.updateWeEmpleCode(weEmpleCode);
        //更新新客自动拉群信息
        communityNewGroup.setNewGroupId(weGroupCode.getId());
        communityNewGroup.setEmpleCodeId(weEmpleCode.getId());
        communityNewGroup.setEmpleCodeName(communityNewGroupDto.getActivityScene());
        communityNewGroup.setCreateBy(SecurityUtils.getUsername());
        this.updateById(communityNewGroup);

        return 1;
    }

    @Override
    public int batchRemoveWeCommunityNewGroupIds(List<String> idList) {
        return weCommunityNewGroupMapper.batchRemoveWeCommunityNewGroupIds(idList);
    }

}
