package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.taggroup.WePresTagGroupTask;
import com.linkwechat.domain.taggroup.query.WePresTagGroupTaskQuery;
import com.linkwechat.domain.taggroup.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 老客户标签建群相关Mapper接口
 */
public interface WePresTagGroupTaskMapper extends BaseMapper<WePresTagGroupTask> {



    /**
     * 获取头部统计
     * @param task
     * @return
     */
    WePresTagGroupTaskTabCountVo countTab(@Param("task") WePresTagGroupTask task);



    /**
     * 获取头部统计
     * @param task
     * @return
     */
    List<WePresTagGroupTaskTrendCountVo> findTrendCountVo(@Param("task") WePresTagGroupTask task);



    /**
     * 获取相关客户
     * @param wePresTagGroupTaskQuery
     * @return
     */
    List<WePresTagGroupTaskTableVo> findWePresTagGroupTaskTable(@Param("query") WePresTagGroupTaskQuery wePresTagGroupTaskQuery);

//    /**
//     * 根据条件查询老客标签建群任务
//     *
//     * @param taskName  任务名称
//     * @param createBy  创建人
//     * @param beginTime 开始时间
//     * @param endTime   结束时间
//     * @param sendType  发送方式
//     * @return 结果
//     */
//    @DataScope(type = "2", value = @DataColumn(alias = "wptg", name = "create_by_id", userid = "user_id"))
//    List<WePresTagTaskListVo> selectListVO(@Param("taskName") String taskName,
//                                           @Param("sendType") Integer sendType,
//                                           @Param("createBy") String createBy,
//                                           @Param("beginTime") String beginTime,
//                                           @Param("endTime") String endTime);
//
//    /**
//     * 获取老客户标签建群任务
//     *
//     * @param taskId 任务id
//     * @return 结果
//     */
//    WePresTagGroupTaskVo selectTaskById(Long taskId);

//
//    /**
//     * 获取某员工的任务
//     *
//     * @param followerId 员工id
//     * @param isDone     已完成的还是待处理
//     * @return 结果
//     */
//    List<WePresTagGroupTaskVo> getTaskListByFollowerId(@Param("followerId") String followerId,
//                                                       @Param("isDone") Integer isDone);

//    /**
//     * 获取指定条件的外部联系人id，且需要排除已经在群的客户
//     *
//     * @param taskId 标签建群任务id
//     * @return 客户的external_userids字段值数组
//     */
//    List<WeCustomer> selectTaskExternalIds(@Param("taskId") Long taskId);
//
//    /**
//     * 获取指定条件的外部联系人id，且需要排除已经在群的客户
//     *
//     * @param taskId 标签建群任务id
//     * @return 客户的external_userids字段值数组
//     */
//    List<WeAddGroupMessageQuery.SenderInfo> selectSenderInfo(@Param("taskId") Long taskId);
//
//    /**
//     * 获取指定条件的员工id
//     *
//     * @param taskId 标签建群任务id
//     * @return 获取目标客户对应员工的id数组
//     */
//    List<String> selectTaskFollowerIds(@Param("taskId") Long taskId);
//
//    /**
//     * 根据任务id和员工id获取客户的external_id
//     *
//     * @param taskId     任务id
//     * @param followerId 跟进者user_id字段
//     * @return 客户external_id列表
//     */
//    List<String> selectTaskExternalByFollowerId(@Param("taskId") Long taskId, @Param("followerId") String followerId);
//
//    /**
//     * 通过taskId获取所有对应群聊的客户的external_id, 用于个人群发的统计
//     *
//     * @param taskId 老客标签建群任务id
//     * @return 结果
//     */
//    List<String> getGroupMemberExternalIdsByTaskId(Long taskId);
}
