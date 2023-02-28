package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeAllocateGroup;
import java.util.List;

public interface IWeAllocateGroupService extends IService<WeAllocateGroup> {

    void batchAddOrUpdate(List<WeAllocateGroup> weAllocateGroups);
}
