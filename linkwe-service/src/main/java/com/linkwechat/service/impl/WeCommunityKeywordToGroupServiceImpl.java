package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.community.WeKeywordGroupTask;
import com.linkwechat.mapper.WeKeywordGroupTaskMapper;
import com.linkwechat.service.IWeCommunityKeywordToGroupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeCommunityKeywordToGroupServiceImpl  extends ServiceImpl<WeKeywordGroupTaskMapper, WeKeywordGroupTask> implements IWeCommunityKeywordToGroupService {
    @Override
    public boolean isNameOccupied(String taskName) {

        List<WeKeywordGroupTask> groupTasks = this.list(new LambdaQueryWrapper<WeKeywordGroupTask>()
                .eq(WeKeywordGroupTask::getTaskName, taskName)
        );
        if(CollectionUtil.isNotEmpty(groupTasks)){
            return true;
        }
        return false;
    }

    @Override
    public List<WeKeywordGroupTask> getTaskList(WeKeywordGroupTask task) {
        return this.baseMapper.getTaskList(task);
    }

    @Override
    public WeKeywordGroupTask getTaskById(Long taskId) {
        return this.baseMapper.getTaskById(taskId);
    }

    @Override
    public List<WeKeywordGroupTask> filterByNameOrKeyword(String word) {
        return this.baseMapper.filterByNameOrKeyword(word);
    }


}
