package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.linkwechat.domain.qirule.query.WeQiRuleWeeklyListQuery;
import com.linkwechat.domain.qirule.vo.WeQiRuleWeeklyListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeQiRuleManageStatistics;

/**
 * 会话质检督导数据表(WeQiRuleManageStatistics)
 *
 * @author danmo
 * @since 2023-05-17 13:50:44
 */
@Repository()
@Mapper
public interface WeQiRuleManageStatisticsMapper extends BaseMapper<WeQiRuleManageStatistics> {


    List<WeQiRuleManageStatistics> getWeeklyList(WeQiRuleWeeklyListQuery query);
}

