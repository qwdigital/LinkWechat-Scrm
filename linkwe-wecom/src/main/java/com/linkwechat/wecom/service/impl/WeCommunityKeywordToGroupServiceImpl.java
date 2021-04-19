package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.WeGroupCode;
import com.linkwechat.wecom.domain.WeKeywordGroupTaskKeyword;
import com.linkwechat.wecom.domain.WeKeywordGroupTask;
import com.linkwechat.wecom.domain.vo.WeGroupCodeVo;
import com.linkwechat.wecom.domain.vo.WeKeywordGroupTaskVo;
import com.linkwechat.wecom.mapper.WeGroupCodeMapper;
import com.linkwechat.wecom.mapper.WeKeywordGroupTaskKwMapper;
import com.linkwechat.wecom.mapper.WeKeywordGroupTaskMapper;
import com.linkwechat.wecom.service.IWeCommunityKeywordToGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 社区运营 - 关键词拉群任务ServiceImpl
 */
@Slf4j
@Service
public class WeCommunityKeywordToGroupServiceImpl extends ServiceImpl<WeKeywordGroupTaskMapper, WeKeywordGroupTask> implements IWeCommunityKeywordToGroupService {

    @Autowired
    private WeKeywordGroupTaskMapper taskMapper;

    @Autowired
    private WeKeywordGroupTaskKwMapper taskKwMapper;

    @Autowired
    private WeGroupCodeMapper groupCodeMapper;

    /**
     * 根据过滤条件获取关键词拉群任务列表
     *
     * @param taskName  任务名称
     * @param createBy  创建人
     * @param keyword   关键词
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 列表数据
     */
    @Override
    public List<WeKeywordGroupTaskVo> getTaskList(String taskName, String createBy, String keyword, String beginTime, String endTime) {
        List<WeKeywordGroupTaskVo> taskVoList = taskMapper.getTaskList(taskName, createBy, keyword, beginTime, endTime);
        for (WeKeywordGroupTaskVo taskVo : taskVoList) {
            // 查询关键词列表
            List<WeKeywordGroupTaskKeyword> taskKeywordList = this.getTaskKeywordList(taskVo.getTaskId());
            if (StringUtils.isNotEmpty(taskKeywordList)) {
                taskVo.setKeywordList(taskKeywordList);
            }
            // 群活码信息
            taskVo.setGroupCodeInfo(this.getGroupVoByTaskId(taskVo.getGroupCodeId()));
            // 通过群活码id查询对应的群
            List<String> groupNameList = taskMapper.getGroupNameListByTaskId(taskVo.getTaskId());
            groupNameList.removeIf(Objects::isNull);
            taskVo.setGroupNameList(groupNameList);
        }

        return taskVoList;
    }

    /**
     * 根据id获取任务性情
     *
     * @param taskId 任务id
     * @return 结果
     */
    @Override
    public WeKeywordGroupTaskVo getTaskById(Long taskId) {
        WeKeywordGroupTaskVo taskVo = taskMapper.getTaskById(taskId);
        // 查询关键词列表
        List<WeKeywordGroupTaskKeyword> keywordList = this.getTaskKeywordList(taskId);
        if (StringUtils.isNotEmpty(keywordList)) {
            taskVo.setKeywordList(keywordList);
        }
        // 群活码
        taskVo.setGroupCodeInfo(this.getGroupVoByTaskId(taskVo.getGroupCodeId()));
        // 群聊名称列表
        List<String> groupNameList = taskMapper.getGroupNameListByTaskId(taskVo.getTaskId());
        groupNameList.removeIf(Objects::isNull);
        taskVo.setGroupNameList(groupNameList);
        return taskVo;
    }

    /**
     * 创建新任务
     *
     * @param task     待存储的对象
     * @param keywords 关键词
     * @return 结果
     */
    @Override
    @Transactional
    public int addTask(WeKeywordGroupTask task, String[] keywords) {
        if (this.save(task)) {
            // 构建关键词对象并存储
            List<WeKeywordGroupTaskKeyword> taskKeywordList = Arrays
                    .stream(keywords)
                    .map(word -> new WeKeywordGroupTaskKeyword(task.getTaskId(), word))
                    .collect(Collectors.toList());
            if (StringUtils.isNotEmpty(taskKeywordList)) {
                taskKwMapper.batchBindsTaskKeyword(taskKeywordList);
            }
            return 1;
        }
        return 0;
    }

    /**
     * 对指定任务进行更新
     *
     * @param task     待更新对象
     * @param keywords 关键词
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTask(WeKeywordGroupTask task, String[] keywords) {
        if (taskMapper.updateById(task) == 1) {
            // 删除原有的关键词
            QueryWrapper<WeKeywordGroupTaskKeyword> taskKwQueryWrapper = new QueryWrapper<>();
            taskKwQueryWrapper.eq("task_id", task.getTaskId());
            taskKwMapper.delete(taskKwQueryWrapper);
            // 再重新插入新的关键词
            List<WeKeywordGroupTaskKeyword> taskKeywordList = Arrays
                    .stream(keywords)
                    .map(word -> new WeKeywordGroupTaskKeyword(task.getTaskId(), word))
                    .collect(Collectors.toList());
            if (StringUtils.isNotEmpty(taskKeywordList)) {
                taskKwMapper.batchBindsTaskKeyword(taskKeywordList);
            }
            return 1;
        }
        return 0;
    }

    /**
     * 根据id列表批量删除任务
     *
     * @param ids id列表
     * @return 删除行数
     */
    @Override
    @Transactional
    public int batchRemoveTaskByIds(Long[] ids) {
        // 移除其所有关键词对象
        QueryWrapper<WeKeywordGroupTaskKeyword> taskKwQueryWrapper = new QueryWrapper<>();
        taskKwQueryWrapper.in("task_id", Arrays.asList(ids));
        taskKwMapper.delete(taskKwQueryWrapper);
        QueryWrapper<WeKeywordGroupTask> taskQueryWrapper = new QueryWrapper<>();
        taskKwQueryWrapper.in("task_id", Arrays.asList(ids));
        return taskMapper.delete(taskQueryWrapper);
    }

    /**
     * 检测任务名是否唯一
     *
     * @param taskName 任务名
     * @return 结果
     */
    @Override
    public boolean taskNameIsUnique(String taskName) {
        return taskMapper.checkNameUnique(taskName) == 0;
    }

    /**
     * 通过名称或者关键词进行过滤
     *
     * @param word 过滤字段
     * @return 结果
     */
    @Override
    public List<WeKeywordGroupTaskVo> filterByNameOrKeyword(String word) {
        List<WeKeywordGroupTaskVo> taskVoList = taskMapper.filterByNameOrKeyword(word);
        taskVoList.forEach(taskVo -> {
            // 关键词列表
            taskVo.setKeywordList(this.getTaskKeywordList(taskVo.getTaskId()));
            List<String> groupNameList = taskMapper.getGroupNameListByTaskId(taskVo.getTaskId());
            // 群活码
            taskVo.setGroupCodeInfo(this.getGroupVoByTaskId(taskVo.getGroupCodeId()));
            // 群名称列表
            groupNameList.removeIf(StringUtils::isNull);
            taskVo.setGroupNameList(groupNameList);
        });
        return taskVoList;
    }

    /**
     * 根据任务id获取关键词列表
     *
     * @param taskId 任务id
     */
    private List<WeKeywordGroupTaskKeyword> getTaskKeywordList(Long taskId) {
        QueryWrapper<WeKeywordGroupTaskKeyword> taskKeywordQueryWrapper = new QueryWrapper<>();
        taskKeywordQueryWrapper.eq("task_id", taskId);
        return taskKwMapper.selectList(taskKeywordQueryWrapper);
    }

    /**
     * 获取群活码简略信息
     *
     * @param groupCodeId 群活码id
     * @return 群活码简略信息
     */
    private WeGroupCodeVo getGroupVoByTaskId(Long groupCodeId) {
        WeGroupCode groupCode = groupCodeMapper.selectWeGroupCodeById(groupCodeId);
        WeGroupCodeVo groupCodeVo = new WeGroupCodeVo();
        groupCodeVo.setId(groupCode.getId());
        groupCodeVo.setCodeUrl(groupCode.getCodeUrl());
        groupCodeVo.setUuid(groupCode.getUuid());
        return groupCodeVo;
    }
}
