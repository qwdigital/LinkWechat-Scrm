package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeKeywordGroupTask;

import java.util.List;

/**
 * 社区运营 - 关键词拉群任务Service
 */
public interface IWeCommunityKeywordToGroupService extends IService<WeKeywordGroupTask> {

    /**
     * 根据过滤条件获取关键词拉群任务列表
     * @param task 查询信息
     * @return 结果
     */
    List<WeKeywordGroupTask> getTaskList(WeKeywordGroupTask task);

    /**
     * 根据id获取任务性情
     *
     * @param taskId 任务id
     * @return 结果
     */
    WeKeywordGroupTask getTaskById(Long taskId);

    /**
     * 创建新任务
     *
     * @param task 任务信息
     * @return 结果
     */
    int addTask(WeKeywordGroupTask task);

    /**
     * 对指定任务进行更新
     * @param task 任务信息
     * @return 结果
     */
    int updateTask(WeKeywordGroupTask task);

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
     * @param task 任务信息
     * @return 结果
     */
    boolean isNameOccupied(WeKeywordGroupTask task);

    /**
     * 通过名称或者关键词进行过滤
     *
     * @param word 过滤字段
     * @return 结果
     */
    List<WeKeywordGroupTask> filterByNameOrKeyword(String word);
}
