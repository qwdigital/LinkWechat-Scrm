package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WePresTagGroupTaskTag;
import com.linkwechat.wecom.domain.WeTag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 老客户标签建群任务tag mapper接口
 */
@Mapper
@Repository
public interface WePresTagGroupTaskTagMapper extends BaseMapper<WePresTagGroupTaskTag> {

    /**
     * 通过标签建群任务id获取该任务对应所有标签
     *
     * @param taskId 建群任务id
     * @return 标签Vo列表
     */
    List<WeTag> getTagListByTaskId(Long taskId);

    /**
     * 批量绑定建群任务与标签
     *
     * @param taskTagList 任务-标签关联对象
     * @return 结果
     */
    int batchBindsTaskTags(List<WePresTagGroupTaskTag> taskTagList);

    /**
     * 通过任务id获取所有符合该任务标签的客户的external_id
     *
     * @param taskId 热任务id
     * @return 结果
     */
    List<String> getExternalUserIdListByTaskId(Long taskId);
}
