package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.WePresTagGroupTaskDto;
import com.linkwechat.wecom.domain.vo.WePresTagGroupTaskStatVo;
import com.linkwechat.wecom.domain.vo.WePresTagGroupTaskVo;
import com.linkwechat.wecom.domain.vo.WeEmplVo;
import com.linkwechat.wecom.mapper.WePresTagGroupTaskMapper;
import com.linkwechat.wecom.mapper.WePresTagGroupTaskScopeMapper;
import com.linkwechat.wecom.mapper.WePresTagGroupTaskStatMapper;
import com.linkwechat.wecom.mapper.WePresTagGroupTaskTagMapper;
import com.linkwechat.wecom.service.IWePresTagGroupTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WePresTagGroupTaskServiceImpl extends ServiceImpl<WePresTagGroupTaskMapper, WePresTagGroupTask> implements IWePresTagGroupTaskService {

    @Autowired
    private WePresTagGroupTaskMapper taskMapper;

    @Autowired
    private WePresTagGroupTaskStatMapper taskStatMapper;

    @Autowired
    private WePresTagGroupTaskScopeMapper taskScopeMapper;

    @Autowired
    private WePresTagGroupTaskTagMapper taskTagMapper;

    /**
     * 新增建群任务
     *
     * @param taskDto 建群所需数据
     * @return 数据库新增行数
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int add(WePresTagGroupTaskDto taskDto) {

        // 创建WeCommunityOldGroup对象进行存储
        WePresTagGroupTask task = new WePresTagGroupTask();

        BeanUtils.copyProperties(taskDto, task);
        task.setCreateBy(SecurityUtils.getUsername());

        if (this.save(task)) {
            // 保存标签对象
            List<String> tagIdList = taskDto.getTagList();
            if (CollectionUtil.isNotEmpty(tagIdList)) {
                List<WePresTagGroupTaskTag> taskTagList = tagIdList.stream().map(id -> new WePresTagGroupTaskTag(task.getTaskId(), id)).collect(Collectors.toList());
                taskTagMapper.batchBindsTaskTags(taskTagList);
            }

            // 保存员工信息
            List<String> emplIdList = taskDto.getScopeList();
            if (CollectionUtil.isNotEmpty(emplIdList)) {
                List<WePresTagGroupTaskScope> wePresTagGroupTaskScopeList = emplIdList.stream().map(id -> new WePresTagGroupTaskScope(task.getTaskId(), id)).collect(Collectors.toList());
                taskScopeMapper.batchBindsTaskScopes(wePresTagGroupTaskScopeList);
            }

            return 1;
        }
        return 0;
    }

    /**
     *  根据条件查询任务列表
     * @param taskName 任务名称
     * @param sendType 发送方式
     * @param createBy 创建人
     * @param beginTime 起始时间
     * @param endTime 结束时间
     * @return 结果
     */
    @Override
    public List<WePresTagGroupTaskVo> selectTaskListList(String taskName, Integer sendType, String createBy, String beginTime, String endTime) {
        // 查询任务列表
        List<WePresTagGroupTaskVo> taskVoList = taskMapper.selectTaskList(taskName, sendType, createBy, beginTime, endTime);
        if(CollectionUtil.isNotEmpty(taskVoList)) {

            for (WePresTagGroupTaskVo taskVo : taskVoList) {
                Long taskId = taskVo.getTaskId();
                // 根据任务id获取标签
                List<WeTag> TagList = taskTagMapper.getTagListByTaskId(taskId);
                taskVo.setTagList(TagList);

                // 根据任务id获取该任务使用人员列表
                List<WeEmplVo> emplList = taskScopeMapper.getScopeListByTaskId(taskId);
                taskVo.setScopeList(emplList);
            }
        }
        return taskVoList;
    }

    /**
     * 通过id获取老客标签建群任务
     *
     * @param taskId 任务id
     * @return 结果
     */
    @Override
    public WePresTagGroupTaskVo getTaskById(Long taskId) {
        return taskMapper.selectTaskById(taskId);
    }

    /**
     * 批量删除老客标签建群任务
     *
     * @param idList 任务id列表
     * @return 删除的行数
     */
    @Override
    @Transactional
    public int batchRemoveTaskByIds(Long[] idList) {
        List<Long> ids = Arrays.asList(idList);

        // 解除关联的标签
        QueryWrapper<WePresTagGroupTaskTag> taskTagQueryWrapper = new QueryWrapper<>();
        taskTagQueryWrapper.in("task_id", ids);
        taskTagMapper.delete(taskTagQueryWrapper);
        // 解除关联的员工
        QueryWrapper<WePresTagGroupTaskScope> taskScopeQueryWrapper = new QueryWrapper<>();
        taskScopeQueryWrapper.in("task_id", ids);
        taskScopeMapper.delete(taskScopeQueryWrapper);
        // 删除其用户统计
        QueryWrapper<WePresTagGroupTaskStat> statQueryWrapper = new QueryWrapper<>();
        statQueryWrapper.in("task_id", ids);
        taskStatMapper.delete(statQueryWrapper);

        // 删除task
        QueryWrapper<WePresTagGroupTask> taskQueryWrapper = new QueryWrapper<>();
        taskQueryWrapper.in("task_id", ids);
        return taskMapper.delete(taskQueryWrapper);
    }

    /**
     * 更新老客户标签建群任务
     *
     * @taskId 待更新任务id
     * @param wePresTagGroupTaskDto 更新数据
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int updateTask(Long taskId, WePresTagGroupTaskDto wePresTagGroupTaskDto) {
        WePresTagGroupTask wePresTagGroupTask = new WePresTagGroupTask();
        BeanUtils.copyProperties(wePresTagGroupTaskDto, wePresTagGroupTask);
        wePresTagGroupTask.setTaskId(taskId);
        wePresTagGroupTask.setUpdateBy(SecurityUtils.getUsername());
        if (taskMapper.updateById(wePresTagGroupTask) == 1) {
            // 更新标签
            // 先删除旧标签
            QueryWrapper<WePresTagGroupTaskTag> taskTagQueryWrapper = new QueryWrapper<>();
            taskTagQueryWrapper.eq("task_id", taskId);
            taskTagMapper.delete(taskTagQueryWrapper);
            // 再添加新标签
            List<String> tagIdList = wePresTagGroupTaskDto.getTagList();
            if (CollectionUtil.isNotEmpty(tagIdList)) {
                List<WePresTagGroupTaskTag> wePresTagGroupTaskTagList = tagIdList.stream().map(id -> new WePresTagGroupTaskTag(taskId, id)).collect(Collectors.toList());
                taskTagMapper.batchBindsTaskTags(wePresTagGroupTaskTagList);
            }

            // 更新范围

            // 先解除旧的员工绑定信息
            QueryWrapper<WePresTagGroupTaskScope> taskScopeQueryWrapper = new QueryWrapper<>();
            taskScopeQueryWrapper.eq("task_id", taskId);
            taskScopeMapper.delete(taskScopeQueryWrapper);

            // 再重新绑定员工信息
            List<String> userIdList = wePresTagGroupTaskDto.getScopeList();
            if (CollectionUtil.isNotEmpty(userIdList)) {
                List<WePresTagGroupTaskScope> wePresTagGroupTaskScopeList = userIdList.stream().map(id -> new WePresTagGroupTaskScope(taskId, id)).collect(Collectors.toList());
                taskScopeMapper.batchBindsTaskScopes(wePresTagGroupTaskScopeList);
            }
            return 1;
        }
        return 0;
    }

    /**
     * 检测任务名是否已存在
     *
     * @param taskName 任务名
     * @return 结果
     */
    @Override
    public boolean checkTaskNameUnique(String taskName) {
        int count = taskMapper.checkTaskNameUnique(taskName);
        return count <= 0;
    }

    /**
     * 通过老客标签建群id获取其统计信息
     *
     * @param taskId       任务id
     * @param customerName 客户名
     * @param isSent       是否已发送
     * @param isInGroup    是否已在群
     * @return 统计信息
     */
    @Override
    public List<WePresTagGroupTaskStatVo> getStatByTaskId(Long taskId, String customerName, Integer isSent, Integer isInGroup) {
        return taskStatMapper.getStatByTaskId(taskId, customerName, isSent, isInGroup);
    }

    /**
     * 通过任务id获取对应使用员工
     *
     * @param taskId 任务id
     * @return 结果
     */
    @Override
    public List<WeEmplVo> getEmplListByTaskId(Long taskId) {
        return taskScopeMapper.getScopeListByTaskId(taskId);
    }

    /**
     * 通过任务id获取标签列表
     *
     * @param taskId 任务id
     * @return 结果
     */
    @Override
    public List<WeTag> getTagListByTaskId(Long taskId) {
        return taskTagMapper.getTagListByTaskId(taskId);
    }
}
