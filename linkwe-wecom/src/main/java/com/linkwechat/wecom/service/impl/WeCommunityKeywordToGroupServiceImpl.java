package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.wecom.domain.WeKeywordGroupTask;
import com.linkwechat.wecom.mapper.WeKeywordGroupTaskMapper;
import com.linkwechat.wecom.service.IWeCommunityKeywordToGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 社区运营 - 关键词拉群任务ServiceImpl
 */
@Slf4j
@Service
public class WeCommunityKeywordToGroupServiceImpl extends ServiceImpl<WeKeywordGroupTaskMapper, WeKeywordGroupTask> implements IWeCommunityKeywordToGroupService {

    /**
     * 根据过滤条件获取关键词拉群任务列表
     * @param task 查询信息
     * @return 结果
     */
    @Override
    public List<WeKeywordGroupTask> getTaskList(WeKeywordGroupTask task) {
        return baseMapper.getTaskList(task);
    }

    /**
     * 根据id获取任务性情
     *
     * @param taskId 任务id
     * @return 结果
     */
    @Override
    public WeKeywordGroupTask getTaskById(Long taskId) {
        return baseMapper.getTaskById(taskId);
    }

    /**
     * 创建新任务
     *
     * @param task 任务信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addTask(WeKeywordGroupTask task) {
        return baseMapper.insert(task);
    }

    /**
     * 对指定任务进行更新
     * @param task 任务信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTask(WeKeywordGroupTask task) {
        return baseMapper.updateById(task);
    }

    /**
     * 根据id列表批量删除任务
     *
     * @param ids id列表
     * @return 删除行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchRemoveTaskByIds(Long[] ids) {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 通过名称或者关键词进行过滤
     *
     * @param word 过滤字段
     * @return 结果
     */
    @Override
    public List<WeKeywordGroupTask> filterByNameOrKeyword(String word) {
        return baseMapper.filterByNameOrKeyword(word);
    }

    /**
     * 任务名是否已被占用
     *
     * @param task 任务信息
     * @return 结果
     */
    @Override
    public boolean isNameOccupied(WeKeywordGroupTask task) {
        Long currentId = Optional.ofNullable(task.getTaskId()).orElse(-1L);
        LambdaQueryWrapper<WeKeywordGroupTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeKeywordGroupTask::getTaskName, task.getTaskName());
        List<WeKeywordGroupTask> queryRes = baseMapper.selectList(queryWrapper);
        return  !queryRes.isEmpty() && !currentId.equals(queryRes.get(0).getTaskId());
    }
}
