package com.linkwechat.wecom.mapper;

import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.vo.SenderInfo;
import com.linkwechat.wecom.domain.vo.WePresTagGroupTaskVo;
import com.linkwechat.wecom.domain.vo.WePresTagTaskListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WePresTagGroupTask;

import java.util.List;

/**
 * 老客户标签建群相关Mapper接口
 */
@Mapper
public interface WePresTagGroupTaskMapper extends BaseMapper<WePresTagGroupTask> {

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
    List<WePresTagTaskListVO> selectListVO(@Param("taskName") String taskName,
                                           @Param("sendType") Integer sendType,
                                           @Param("createBy") String createBy,
                                           @Param("beginTime") String beginTime,
                                           @Param("endTime") String endTime);

    /**
     * 获取老客户标签建群任务
     *
     * @param taskId 任务id
     * @return 结果
     */
    WePresTagGroupTaskVo selectTaskById(Long taskId);


    /**
     * 获取某员工的任务
     *
     * @param followerId 员工id
     * @param isDone 已完成的还是待处理
     * @return 结果
     */
    List<WePresTagGroupTaskVo> getTaskListByFollowerId(@Param("followerId") String followerId, @Param("isDone") Integer isDone);

    /**
     * 获取指定条件的外部联系人id，且需要排除已经在群的客户
     *
     * @param taskId 标签建群任务id
     * @return 客户的external_userids字段值数组
     */
    List<WeCustomer> selectTaskExternalIds(@Param("taskId") Long taskId);

    /**
     * 获取指定条件的外部联系人id，且需要排除已经在群的客户
     *
     * @param taskId 标签建群任务id
     * @return 客户的external_userids字段值数组
     */
    List<SenderInfo> selectSenderInfo(@Param("taskId") Long taskId);

    /**
     * 获取指定条件的员工id
     *
     * @param taskId 标签建群任务id
     * @return 获取目标客户对应员工的id数组
     */
    List<String> selectTaskFollowerIds(@Param("taskId") Long taskId);

    /**
     * 根据任务id和员工id获取客户的external_id
     *
     * @param taskId     任务id
     * @param followerId 跟进者user_id字段
     * @return 客户external_id列表
     */
    List<String> selectTaskExternalByFollowerId(@Param("taskId") Long taskId, @Param("followerId") String followerId);

    /**
     * 通过taskId获取所有对应群聊的客户的external_id, 用于个人群发的统计
     *
     * @param taskId 老客标签建群任务id
     * @return 结果
     */
    List<String> getGroupMemberExternalIdsByTaskId(Long taskId);
}
