package com.linkwechat.wecom.service;

import com.linkwechat.wecom.domain.dto.WeKeywordGroupTaskDto;
import com.linkwechat.wecom.domain.vo.WeKeywordGroupTaskVo;

import java.util.List;

/**
 * 社区运营 - 关键词拉群任务Service
 */
public interface IWeCommunityKeywordToGroupService {

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
    List<WeKeywordGroupTaskVo> getTaskList(String taskName, String createBy, String keyword, String beginTime, String endTime);

    /**
     * 根据id获取任务性情
     *
     * @param taskId 任务id
     * @return 结果
     */
    WeKeywordGroupTaskVo getTaskById(Long taskId);

    /**
     * 创建新任务
     *
     * @param taskDto 任务信息
     * @return 结果
     */
    int addTask(WeKeywordGroupTaskDto taskDto);

    /**
     * 对指定任务进行更新
     * @param taskId 待更新任务的id
     * @param taskDto 任务信息
     * @return 结果
     */
    int updateTask(Long taskId, WeKeywordGroupTaskDto taskDto);

    /**
     * 根据id列表批量删除任务
     *
     * @param ids id列表
     * @return 删除行数
     */
    int batchRemoveTaskByIds(Long[] ids);

    /**
     * 检测任务名是否唯一
     *
     * @param taskName 任务名
     * @return 结果
     */
    boolean taskNameIsUnique(String taskName);


    /**
     * 通过名称或者关键词进行过滤
     *
     * @param word 过滤字段
     * @return 结果
     */
    List<WeKeywordGroupTaskVo> filterByNameOrKeyword(String word);
}
