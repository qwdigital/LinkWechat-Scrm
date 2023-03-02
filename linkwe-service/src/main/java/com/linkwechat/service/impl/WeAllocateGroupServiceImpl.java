package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeAllocateGroup;
import com.linkwechat.mapper.WeAllocateGroupMapper;
import com.linkwechat.service.IWeAllocateGroupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeAllocateGroupServiceImpl extends ServiceImpl<WeAllocateGroupMapper, WeAllocateGroup> implements IWeAllocateGroupService {
    @Override
    public void batchAddOrUpdate(List<WeAllocateGroup> weAllocateGroups) {
        this.baseMapper.batchAddOrUpdate(weAllocateGroups);
    }
}
