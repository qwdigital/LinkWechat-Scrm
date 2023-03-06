package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeAllocateCustomer;
import com.linkwechat.domain.WeAllocateGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 分配的群租Mapper接口
 *
 * @author ruoyi
 * @date 2020-10-24
 */
public interface WeAllocateGroupMapper extends BaseMapper<WeAllocateGroup>
{
    void batchAddOrUpdate(@Param("weAllocateGroups") List<WeAllocateGroup> weAllocateGroups);
}
