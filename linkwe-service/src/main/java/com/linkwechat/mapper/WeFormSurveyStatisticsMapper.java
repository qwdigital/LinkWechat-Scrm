package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeFormSurveyStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 问卷-统计表(WeFormSurveyStatistics)
 *
 * @author danmo
 * @since 2022-09-20 18:02:57
 */
@Repository()
@Mapper
public interface WeFormSurveyStatisticsMapper extends BaseMapper<WeFormSurveyStatistics> {


}

