package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeQiRuleUserStatistics;

/**
 * 会话质检员工数据统计表(WeQiRuleUserStatistics)
 *
 * @author danmo
 * @since 2023-05-17 13:50:44
 */
@Repository()
@Mapper
public interface WeQiRuleUserStatisticsMapper extends BaseMapper<WeQiRuleUserStatistics> {


}

