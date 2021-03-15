package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeKeywordGroupTaskKeyword;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 关键词拉群 关键词mapper
 */
@Mapper
@Repository
public interface WeKeywordGroupTaskKwMapper extends BaseMapper<WeKeywordGroupTaskKeyword> {

    /**
     * 批量绑定关键词拉群任务与关键词关联
     * @param taskKeywordList 待绑定对象
     * @return 结果
     */
    int batchBindsTaskKeyword(List<WeKeywordGroupTaskKeyword> taskKeywordList);
}
