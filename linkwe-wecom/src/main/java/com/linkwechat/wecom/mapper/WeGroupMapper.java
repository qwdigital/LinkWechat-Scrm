package com.linkwechat.wecom.mapper;

import com.linkwechat.wecom.domain.WeGroup;

import java.util.List;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/21 0021 23:58
 */
public interface WeGroupMapper {
    WeGroup selectWeGroupById(Long paramLong);

    List<WeGroup> selectWeGroupList(WeGroup paramWeGroup);

    int insertWeGroup(WeGroup paramWeGroup);

    int updateWeGroup(WeGroup paramWeGroup);

    int deleteWeGroupById(Long paramLong);

    int deleteWeGroupByIds(Long[] paramArrayOfLong);
}
