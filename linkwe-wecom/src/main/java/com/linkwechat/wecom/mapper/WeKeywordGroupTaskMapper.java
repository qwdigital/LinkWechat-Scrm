package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeKeywordGroupTask;
import com.linkwechat.wecom.domain.vo.WeKeywordGroupTaskVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 关键词拉群mapper
 */
@Mapper
@Repository
public interface WeKeywordGroupTaskMapper extends BaseMapper<WeKeywordGroupTask> {
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
    List<WeKeywordGroupTaskVo> getTaskList(
            @Param("taskName") String taskName,
            @Param("createBy") String createBy,
            @Param("keyword") String keyword,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime
    );

    /**
     * 根据id获取任务性情
     *
     * @param taskId 任务id
     * @return 结果
     */
    WeKeywordGroupTaskVo getTaskById(Long taskId);

    /**
     * 根据id列表批量删除任务
     *
     * @param ids id列表
     * @return 删除行数
     */
    int batchRemoveTaskByIds(Long[] ids);

    /**
     * 根据任务id获取对应所有的群聊名称
     *
     * @param taskId 任务id
     * @return 结果
     */
    List<String> getGroupNameListByTaskId(Long taskId);

    /**
     * 校验名称是否唯一
     *
     * @param taskName 任务名
     * @return 结果
     */
    int checkNameUnique(String taskName);
}
