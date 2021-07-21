package com.linkwechat.wecom.mapper;

import com.linkwechat.wecom.domain.vo.WePresTagGroupTaskVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WePresTagGroupTask;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * 老客户标签建群相关Mapper接口
 */
@Mapper
public interface WePresTagGroupTaskMapper extends BaseMapper<WePresTagGroupTask> {

    /**
     * 添加新任务
     * @param task 老客标签建群任务
     * @return 结果
     */
    int insertTask(WePresTagGroupTask task);

    /**
     * 更新任务
     * @param task 建群任务信息
     * @return 结果
     */
    int updateTask(WePresTagGroupTask task);
    /**
     * 获取老客户标签建群任务
     *
     * @param taskId 任务id
     * @return 结果
     */
    WePresTagGroupTaskVo selectTaskById(Long taskId);

    /**
     * 根据条件查询老客标签建群任务
     *
     * @param taskName  任务名称
     * @param createBy  创建人
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @param sendType  发送方式
     * @return 结果
     */
    List<WePresTagGroupTaskVo> selectTaskList(
            @Param("taskName") String taskName,
            @Param("sendType") Integer sendType,
            @Param("createBy") String createBy,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime
    );

    /**
     * 获取某员工的任务
     *
     * @param emplId 员工id
     * @param isDone 已完成的还是待处理
     * @return 结果
     */
    List<WePresTagGroupTaskVo> getTaskListByEmplId(@Param("emplId") String emplId, @Param("isDone") boolean isDone);

    /**
     * 根据task获取员工信息
     * @param task
     * @return
     */
    List<String> selectStaffList(WePresTagGroupTask task);

    List<String> selectExternalUserIds(
            @Param("taskId") Long taskId,
            @Param("hasScope") boolean hasScope,
            @Param("hasTag") boolean hasTag,
            @Param("gender") Integer gender,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime
    );
}
