package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.community.WeKeywordGroupTask;

import java.io.IOException;
import java.util.List;

/**
 * 社区运营 - 关键词拉群任务Service
 */
public interface IWeCommunityKeywordToGroupService extends IService<WeKeywordGroupTask> {


    /**
     * 获取数据列表
     * @param task
     * @return
     */
    List<WeKeywordGroupTask> findLists(WeKeywordGroupTask task);


    /**
     * 查看基础消息，是否统计埋点
     * @param id 关键词主键
     * @param isCount true 统计 false不统计
     * @return
     */
    WeKeywordGroupTask findBaseInfo(Long id,String unionId,Boolean isCount);


    /**
     * 新建或更新关键词群信息
     * @param groupTask
     */
    void createOrUpdate(WeKeywordGroupTask groupTask) throws IOException;

//    /**
//     * 任务名是否已被占用
//     *
//     * @param taskName 任务名称
//     * @return 结果
//     */
//     boolean isNameOccupied(String taskName);
//
//    /**
//     * 根据过滤条件获取关键词拉群任务列表
//     * @param task 查询信息
//     * @return 结果
//     */
//    List<WeKeywordGroupTask> getTaskList(WeKeywordGroupTask task);
//
//
//
//
//    /**
//     * 根据id获取任务性情
//     *
//     * @param taskId 任务id
//     * @return 结果
//     */
//    WeKeywordGroupTask getTaskById(Long taskId);
//
//
//
//    /**
//     * 通过名称或者关键词进行过滤
//     *
//     * @param word 过滤字段
//     * @return 结果
//     */
//    List<WeKeywordGroupTask> filterByNameOrKeyword(String word);


}
