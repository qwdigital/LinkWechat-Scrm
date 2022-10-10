package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeAllocateGroup;
import com.linkwechat.mapper.WeAllocateGroupMapper;
import com.linkwechat.service.IWeAllocateGroupService;
import org.springframework.stereotype.Service;

@Service
public class WeAllocateGroupServiceImpl extends ServiceImpl<WeAllocateGroupMapper, WeAllocateGroup> implements IWeAllocateGroupService {
}
