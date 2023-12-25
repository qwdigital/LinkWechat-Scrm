package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeKeywordGroupViewCount;
import com.linkwechat.domain.community.query.WeCommunityKeyWordGroupTableQuery;
import com.linkwechat.domain.community.vo.WeCommunityKeyWordGroupTableVo;
import com.linkwechat.domain.community.vo.WeKeywordGroupViewCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author robin
* @description 针对表【we_keyword_group_view_count(关键词群访问统计)】的数据库操作Mapper
* @createDate 2023-12-19 12:33:29
* @Entity com.linkwechat.WeKeywordGroupViewCount
*/
public interface WeKeywordGroupViewCountMapper extends BaseMapper<WeKeywordGroupViewCount> {


    /**
     * 折现统计图
     * @param states
     * @param keyWordGroupId
     * @param beginTime
     * @param endTime
     * @return
     */
    List<WeKeywordGroupViewCountVo> countTrend(@Param("states") List<String> states,@Param("keyWordGroupId") Long keyWordGroupId,@Param("beginTime") String beginTime,@Param("endTime") String endTime);


    /**
     * 数据明细
     * @param query
     * @return
     */
    List<WeCommunityKeyWordGroupTableVo> findKeyWordGroupTable(@Param("query") WeCommunityKeyWordGroupTableQuery query);
}




