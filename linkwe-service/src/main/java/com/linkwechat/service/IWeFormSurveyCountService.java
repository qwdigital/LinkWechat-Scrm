package com.linkwechat.service;

import com.linkwechat.domain.WeFormSurveyCount;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeFormSurveyStatistics;
import com.linkwechat.domain.form.vo.WeFormSurveyStatisticsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author robin
* @description 针对表【we_form_survey_count(智能表单统计(按照每天的维度统计相关客户数据；ip+当天定位每一条记录))】的数据库操作Service
* @createDate 2023-12-11 18:21:45
*/
public interface IWeFormSurveyCountService extends IService<WeFormSurveyCount> {


    /**
     * 访问统计
     * @param belongId
     * @param visitorIp
     */
    void weFormCount(Long belongId,String dataSource,String visitorIp);


    /**
     * 设置总访问时长
     * @param belongId
     * @param visitorIp
     * @param dataSource
     * @param visitTime
     */
    void setVisitTime(Long belongId,String visitorIp,String dataSource,Long visitTime);


    /**
     * 查询头部统计
     * @param weFormSurveyCount
     * @return
     */
    WeFormSurveyStatistics getStatistics(WeFormSurveyCount weFormSurveyCount);


    /**
     * 数据报表
     * @param weFormSurveyCount
     * @return
     */
    List<WeFormSurveyStatistics> findDataList(WeFormSurveyCount weFormSurveyCount);


    /**
     * 折线图
     * @param weFormSurveyCount
     * @return
     */
    List<WeFormSurveyStatistics> lineChart(WeFormSurveyCount weFormSurveyCount);


    /**
     * 总访问量
     * @param belongId
     * @return
     */
    Integer sumTotalVisits(@Param("belongId") Long belongId);


}
