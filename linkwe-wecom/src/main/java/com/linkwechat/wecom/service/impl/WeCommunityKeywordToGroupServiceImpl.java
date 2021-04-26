package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.WeGroupCode;
import com.linkwechat.wecom.domain.WeKeywordGroupTaskKeyword;
import com.linkwechat.wecom.domain.WeKeywordGroupTask;
import com.linkwechat.wecom.domain.dto.WeKeywordGroupTaskDto;
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

import java.util.*;
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
            setGroupCodeVoByTask(taskVo);
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
        setGroupCodeVoByTask(taskVo);
        // 群聊名称列表
        List<String> groupNameList = taskMapper.getGroupNameListByTaskId(taskVo.getTaskId());
        groupNameList.removeIf(Objects::isNull);
        taskVo.setGroupNameList(groupNameList);
        return taskVo;
    }

    /**
     * 创建新任务
     *
     * @param taskDto 任务信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addTask(WeKeywordGroupTaskDto taskDto) {
        validation(taskDto, true);
        WeKeywordGroupTask task = WeKeywordGroupTask.builder()
                .groupCodeId(taskDto.getGroupCodeId())
                .taskName(taskDto.getTaskName())
                .welcomeMsg(taskDto.getWelcomeMsg())
                .build();

        if (this.save(task)) {
            // 构建关键词对象并存储,需要先校验关键词长度
            List<WeKeywordGroupTaskKeyword> taskKeywordList = parseLongKeywordsString(taskDto.getKeywords())
                    .stream()
                    .map(keyword -> new WeKeywordGroupTaskKeyword(task.getTaskId(), keyword))
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
     * @param taskId 待更新任务的id
     * @param taskDto 任务信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTask(Long taskId, WeKeywordGroupTaskDto taskDto) {
        validation(taskDto, false);
        WeKeywordGroupTask task = WeKeywordGroupTask.builder()
                .groupCodeId(taskDto.getGroupCodeId())
                .taskName(taskDto.getTaskName())
                .welcomeMsg(taskDto.getWelcomeMsg())
                .taskId(taskId)
                .build();
        if (taskMapper.updateById(task) == 1) {
            // 删除原有的关键词
            LambdaQueryWrapper<WeKeywordGroupTaskKeyword> keywordWrapper = new LambdaQueryWrapper<>();
            keywordWrapper.eq(WeKeywordGroupTaskKeyword::getTaskId, task.getTaskId());
            taskKwMapper.delete(keywordWrapper);

            // 再重新插入新的关键词
            List<WeKeywordGroupTaskKeyword> taskKeywordList = parseLongKeywordsString(taskDto.getKeywords())
                    .stream()
                    .map(keyword -> new WeKeywordGroupTaskKeyword(task.getTaskId(), keyword))
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
    @Transactional(rollbackFor = Exception.class)
    public int batchRemoveTaskByIds(Long[] ids) {
        // 移除其所有关键词对象
        LambdaQueryWrapper<WeKeywordGroupTaskKeyword> keywordWrapper = new LambdaQueryWrapper<>();
        keywordWrapper.in(WeKeywordGroupTaskKeyword::getTaskId, Arrays.asList(ids));
        taskKwMapper.delete(keywordWrapper);

        LambdaQueryWrapper<WeKeywordGroupTask> taskWrapper = new LambdaQueryWrapper<>();
        taskWrapper.in(WeKeywordGroupTask::getTaskId, Arrays.asList(ids));
        return taskMapper.delete(taskWrapper);
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
            setGroupCodeVoByTask(taskVo);
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
        LambdaQueryWrapper<WeKeywordGroupTaskKeyword> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WeKeywordGroupTaskKeyword::getTaskId, taskId);
        return taskKwMapper.selectList(wrapper);
    }

    /**
     * 获取群活码简略信息
     * @param taskVo 关键词任务信息
     */
    private void setGroupCodeVoByTask(WeKeywordGroupTaskVo taskVo) {
        WeGroupCode groupCode = groupCodeMapper.selectWeGroupCodeById(taskVo.getGroupCodeId());
        Optional.ofNullable(groupCode).ifPresent(code -> {
            WeGroupCodeVo groupCodeVo = new WeGroupCodeVo();
            groupCodeVo.setId(code.getId());
            groupCodeVo.setCodeUrl(code.getCodeUrl());
            groupCodeVo.setUuid(code.getUuid());
            taskVo.setGroupCodeInfo(groupCodeVo);
        });
    }
    /**
     * 校验数据
     * @param taskDto 待校验数据
     * @param addMode 是否是新增模式(更新时不需要检测任务名)
     */

    private void validation(WeKeywordGroupTaskDto taskDto, boolean addMode) {
        final int MAX_NAME_LENGTH = 30;
        final int MAX_MSG_LENGTH = 220;

        // 群活码必须存在
        if (null == groupCodeMapper.selectWeGroupCodeById(taskDto.getGroupCodeId())) {
            throw new CustomException("群活码不存在", HttpStatus.BAD_REQUEST);
        }
        // 任务名称必须唯一
        if (!taskNameIsUnique(taskDto.getTaskName()) && addMode) {
            throw new CustomException("任务名称已存在", HttpStatus.BAD_REQUEST);
        }
        // 名称不可超长
        if (taskDto.getTaskName().length() > MAX_NAME_LENGTH) {
            throw new CustomException("活码名称长度超长，请重新输入", HttpStatus.BAD_REQUEST);
        }
        // 引导语不可超长
        if (taskDto.getWelcomeMsg().length() > MAX_MSG_LENGTH) {
            throw new CustomException("引导语长度超长，请重新输入", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 对关键词长字符串进行校验，获取关键词列表
     * @param longKeywordsString 关键词长字符串
     * @return 关键词列表
     */
    private List<String> parseLongKeywordsString(String longKeywordsString) {
        String[] keywords = longKeywordsString.split(",");
        final int MAX_KEYWORD_LENGTH = 10;
        List<String> resultList = new ArrayList<>();
        for (String keyword : keywords) {
            if (keyword.length() > MAX_KEYWORD_LENGTH) {
                throw new CustomException(String.format("关键字 %s 长度过长，请重新输入。", keyword), HttpStatus.BAD_REQUEST);
            }
            resultList.add(keyword);
        }
        return resultList;
    }
}
