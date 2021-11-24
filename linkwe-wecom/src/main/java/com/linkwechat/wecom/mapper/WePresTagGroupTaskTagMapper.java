package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WePresTagGroupTaskTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 老客户标签建群任务tag mapper接口
 */
@Mapper
public interface WePresTagGroupTaskTagMapper extends BaseMapper<WePresTagGroupTaskTag> {

    /**
     * 批量绑定建群任务与标签
     *
     * @param taskTagList 任务-标签关联对象
     * @return 结果
     */
    int batchBindsTaskTags(List<WePresTagGroupTaskTag> taskTagList);
}
