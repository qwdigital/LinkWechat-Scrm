package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeAllocateGroup;
import com.linkwechat.wecom.mapper.WeAllocateGroupMapper;
import com.linkwechat.wecom.service.IWeAllocateGroupService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分配的群租Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-10-24
 */
@Service
public class WeAllocateGroupServiceImpl extends ServiceImpl<WeAllocateGroupMapper,WeAllocateGroup> implements IWeAllocateGroupService
{

    @Override
    public void batchAddOrUpdate(List<WeAllocateGroup> weAllocateGroupList) {
        this.baseMapper.batchAddOrUpdate(weAllocateGroupList);
    }
}
