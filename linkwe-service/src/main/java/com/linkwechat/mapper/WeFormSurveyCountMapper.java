package com.linkwechat.mapper;

import com.linkwechat.domain.WeFormSurveyCount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeFormSurveyStatistics;
import com.linkwechat.domain.form.vo.WeFormSurveyStatisticsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author robin
* @description 针对表【we_form_survey_count(智能表单统计(按照每天的维度统计相关客户数据；ip+当天定位每一条记录))】的数据库操作Mapper
* @createDate 2023-12-11 18:21:45
* @Entity com.linkwechat.domain.WeFormSurveyCount
*/
public interface WeFormSurveyCountMapper extends BaseMapper<WeFormSurveyCount> {

    WeFormSurveyStatistics getStatistics(@Param("weFormSurveyCount") WeFormSurveyCount weFormSurveyCount);


    List<WeFormSurveyStatistics> findDataList(@Param("weFormSurveyCount") WeFormSurveyCount weFormSurveyCount);


     List<WeFormSurveyStatistics> lineChart(@Param("weFormSurveyCount") WeFormSurveyCount weFormSurveyCount);

    Integer sumTotalVisits(@Param("belongId") Long belongId);

}




