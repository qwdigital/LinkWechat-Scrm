package com.linkwechat.wecom.service;

import com.linkwechat.wecom.domain.WeTag;
import com.linkwechat.wecom.domain.dto.WePresTagGroupTaskDto;
import com.linkwechat.wecom.domain.vo.WeEmplVo;
import com.linkwechat.wecom.domain.vo.WePresTagGroupTaskVo;
import com.linkwechat.wecom.domain.vo.WePresTagGroupTaskStatVo;

import java.util.List;

/**
 * 社区运营 老客户标签建群 相关逻辑
 */
public interface IWePresTagGroupTaskService {

    /**
     * 新增建群任务
     *
     * @param taskDto 建群所需数据
     * @return 数据库新增行数
     */
    int add(WePresTagGroupTaskDto taskDto);

    /**
     * 根据条件查询任务列表
     *
     * @param taskName  任务名称
     * @param sendType  发送方式
     * @param createBy  创建人
     * @param beginTime 起始时间
     * @param endTime   结束时间
     * @return 结果
     */
    List<WePresTagGroupTaskVo> selectTaskListList(String taskName, Integer sendType, String createBy, String beginTime, String endTime);

    /**
     * 通过id获取老客标签建群任务
     *
     * @param taskId 任务id
     * @return 结果
     */
    WePresTagGroupTaskVo getTaskById(Long taskId);

    /**
     * 批量删除老客标签建群任务
     *
     * @param idList 任务id列表
     * @return 删除的行数
     */
    int batchRemoveTaskByIds(Long[] idList);

    /**
     * 更新老客户标签建群任务
     *
     * @param wePresTagGroupTaskDto 更新数据
     * @return 结果
     * @taskId 待更新任务id
     */
    int updateTask(Long taskId, WePresTagGroupTaskDto wePresTagGroupTaskDto);

    /**
     * 检测任务名是否已存在
     *
     * @param taskName 任务名
     * @return 结果
     */
    boolean checkTaskNameUnique(String taskName);

    /**
     * 通过老客标签建群id获取其统计信息
     *
     * @param taskId       任务id
     * @param customerName 客户名
     * @param isSent       是否已发送
     * @param isInGroup    是否已在群
     * @return 统计信息
     */
    List<WePresTagGroupTaskStatVo> getStatByTaskId(Long taskId, String customerName, Integer isSent, Integer isInGroup);

    /**
     * 通过任务id获取对应使用员工
     *
     * @param taskId 任务id
     * @return 结果
     */
    List<WeEmplVo> getEmplListByTaskId(Long taskId);

    /**
     * 通过任务id获取标签列表
     *
     * @param taskId 任务id
     * @return 结果
     */
    List<WeTag> getTagListByTaskId(Long taskId);
}
