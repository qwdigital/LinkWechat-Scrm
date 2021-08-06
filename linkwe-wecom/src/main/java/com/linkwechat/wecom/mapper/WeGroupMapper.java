package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeCustomerAddGroup;
import com.linkwechat.wecom.domain.WeGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/21 0021 23:58
 */
public interface WeGroupMapper extends BaseMapper<WeGroup> {
//    WeGroup selectWeGroupById(Long paramLong);
//
    List<WeGroup> selectWeGroupList(WeGroup paramWeGroup);
//
//    int insertWeGroup(WeGroup paramWeGroup);
//
//    int updateWeGroup(WeGroup paramWeGroup);
//
//    int deleteWeGroupById(Long paramLong);
//
//    int deleteWeGroupByIds(Long[] paramArrayOfLong);
//
//    int batchLogicDeleteByIds(@Param("ids") List<Long> ids);
//
//    int batchInsetWeGroup(@Param("weGroups") List<WeGroup> weGroups);

    List<WeCustomerAddGroup> findWeGroupByCustomer(@Param("userId") String userId,@Param("externalUserid") String externalUserid);

    void insertBatch(@Param("weGroups") List<WeGroup> weGroups);
}
