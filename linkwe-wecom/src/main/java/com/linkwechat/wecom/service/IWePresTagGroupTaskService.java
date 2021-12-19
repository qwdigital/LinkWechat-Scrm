package com.linkwechat.wecom.service;

import com.linkwechat.wecom.domain.WePresTagGroupTask;
import com.linkwechat.wecom.domain.WeTag;
import com.linkwechat.wecom.domain.dto.WePresTagGroupTaskDto;
import com.linkwechat.wecom.domain.vo.WeCommunityTaskEmplVo;
import com.linkwechat.wecom.domain.vo.WePresTagGroupTaskVo;
import com.linkwechat.wecom.domain.vo.WePresTagGroupTaskStatVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 社区运营 老客户标签建群 相关逻辑
 */
public interface IWePresTagGroupTaskService {

    /**
     * 添加新标签建群任务
     *
     * @param task       建群任务本体信息
     * @param tagIdList  标签列表
     * @param emplIdList 员工列表
     * @return 结果
     */
    int add(WePresTagGroupTask task, List<String> tagIdList, List<String> emplIdList);

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
    List<WePresTagGroupTaskVo> selectTaskList(String taskName, Integer sendType, String createBy, String beginTime, String endTime);

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
     * @param taskId                待更新任务id
     * @param wePresTagGroupTaskDto 更新数据
     * @return 更新条数
     */
    int updateTask(Long taskId, WePresTagGroupTaskDto wePresTagGroupTaskDto);

    /**
     * 通过老客标签建群id获取其统计信息
     *
     * @param taskId 任务id
     * @return 统计信息
     */
    List<WePresTagGroupTaskStatVo> getStatByTaskId(Long taskId);


    /**
     * 根据任务id获取对应员工信息列表
     *
     * @param taskId 任务id
     * @return 结果
     */
    List<WeCommunityTaskEmplVo> getScopeListByTaskId(Long taskId);

    /**
     * 根据任务id获取对应标签信息列表
     *
     * @param taskId 任务id
     * @return 结果
     */
    List<WeTag> getTagListByTaskId(Long taskId);

    /**
     * 获取员工建群任务信息
     *
     * @param emplId 员工id
     * @param isDone 是否已处理
     * @return 结果
     */
    List<WePresTagGroupTaskVo> getEmplTaskList(String emplId, boolean isDone);

    /**
     * 员工发送信息后，变更其任务状态为 "完成"
     *
     * @param taskId 任务id
     * @param emplId 员工id
     * @return 结果
     */
    int updateEmplTaskStatus(Long taskId, String emplId);

    /**
     * 根据标签建群任务信息发送消息
     *
     * @param task 标签建群任务
     */
    void sendMessage(WePresTagGroupTask task, List<String> externalIds);

    /**
     * 任务名是否已占用
     *
     * @param task 任务信息
     * @return 名称是否占用
     */
    boolean isNameOccupied(WePresTagGroupTask task);

    List<String> selectExternalUserIds(
            Long taskId,
            boolean hasScope,
            boolean hasTag,
            Integer gender,
            String beginTime,
            String endTime
    );
}
