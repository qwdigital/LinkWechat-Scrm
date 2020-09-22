package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.domain.vo.WeLeaveUserInfoAllocateVo;
import com.linkwechat.wecom.mapper.WeGroupMapper;
import com.linkwechat.wecom.service.IWeGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/22 0022 0:03
 */
@Service
public class WeGroupServiceImpl implements IWeGroupService {
    @Autowired
    private WeGroupMapper weGroupMapper;

    public WeGroup selectWeGroupById(Long id) {
        return this.weGroupMapper.selectWeGroupById(id);
    }

    public List<WeGroup> selectWeGroupList(WeGroup weGroup) {
        return this.weGroupMapper.selectWeGroupList(weGroup);
    }

    public int insertWeGroup(WeGroup weGroup) {
        weGroup.setCreateTime(DateUtils.getNowDate());
        return this.weGroupMapper.insertWeGroup(weGroup);
    }

    public int updateWeGroup(WeGroup weGroup) {
        return this.weGroupMapper.updateWeGroup(weGroup);
    }

    public int deleteWeGroupByIds(Long[] ids) {
        return this.weGroupMapper.deleteWeGroupByIds(ids);
    }

    public int deleteWeGroupById(Long id) {
        return this.weGroupMapper.deleteWeGroupById(id);
    }

    /**
     * 逻辑批量删除
     * @param ids
     */
    @Override
    public int batchLogicDeleteByIds(List<Long> ids) {

      return   weGroupMapper.batchLogicDeleteByIds(ids);
    }


    /**
     * 批量保存
     * @param weGroups
     * @return
     */
    @Override
    public int batchInsetWeGroup(List<WeGroup> weGroups) {
        return weGroupMapper.batchInsetWeGroup(weGroups);
    }


    /**
     * 离职员工群分配
     * @param weLeaveUserInfoAllocateVo
     * @return
     */
    @Override
    @Transactional
    public void allocateWeGroup(WeLeaveUserInfoAllocateVo weLeaveUserInfoAllocateVo) {
        //分配群
        List<WeGroup> weGroups = this.selectWeGroupList(WeGroup.builder()
                .groupLeaderUserId(weLeaveUserInfoAllocateVo.getHandoverUserid())
                .build());
        if(CollectionUtil.isNotEmpty(weGroups)){

            this.batchLogicDeleteByIds(
                    weGroups.stream().map(WeGroup::getId).collect(Collectors.toList())
            );

            weGroups.stream().forEach(k->{
                k.setId(SnowFlakeUtil.nextId());
                k.setGroupLeaderUserId(weLeaveUserInfoAllocateVo.getTakeoverUserid());
            });

            this.batchInsetWeGroup(weGroups);
        }
    }

}
