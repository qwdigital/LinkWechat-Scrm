package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeKeywordGroupTask;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 关键词拉群mapper
 */
@Mapper
public interface WeKeywordGroupTaskMapper extends BaseMapper<WeKeywordGroupTask> {
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
     * 通过名称或者关键词进行过滤
     *
     * @param word 过滤字段
     * @return 结果
     */
    List<WeKeywordGroupTask> filterByNameOrKeyword(String word);
}
