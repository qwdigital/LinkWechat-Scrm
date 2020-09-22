package com.linkwechat.wecom.service;

import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.domain.vo.WeLeaveUserInfoAllocateVo;

import java.util.List;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/22 0022 0:01
 */
public interface IWeGroupService {
    WeGroup selectWeGroupById(Long paramLong);

    List<WeGroup> selectWeGroupList(WeGroup paramWeGroup);

    int insertWeGroup(WeGroup paramWeGroup);

    int updateWeGroup(WeGroup paramWeGroup);

    int deleteWeGroupByIds(Long[] paramArrayOfLong);

    int deleteWeGroupById(Long paramLong);

    int batchLogicDeleteByIds(List<Long> ids);

    int batchInsetWeGroup(List<WeGroup> weGroups);

    void allocateWeGroup(WeLeaveUserInfoAllocateVo weLeaveUserInfoAllocateVo);
}
