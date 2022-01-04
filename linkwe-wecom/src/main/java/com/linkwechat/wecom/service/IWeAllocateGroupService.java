package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeAllocateGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分配的群租Service接口
 * 
 * @author ruoyi
 * @date 2020-10-24
 */
public interface IWeAllocateGroupService  extends IService<WeAllocateGroup>
{
    void  batchAddOrUpdate(List<WeAllocateGroup> weAllocateGroupList);
}
