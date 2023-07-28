package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeFormSurveyStatistics;
import com.linkwechat.domain.form.query.WeFormSurveyStatisticQuery;

import java.util.List;

/**
 * 问卷-统计表(WeFormSurveyStatistics)
 *
 * @author danmo
 * @since 2022-09-20 18:02:57
 */
public interface IWeFormSurveyStatisticsService extends IService<WeFormSurveyStatistics> {

    void delStatistics(WeFormSurveyStatistics surveyStatistics);


    /**
     * 获取表单总的统计数据
     *
     * @param query
     * @return {@link List< WeFormSurveyStatistics>}
     * @author WangYX
     * @date 2022/10/14 16:18
     */
    List<WeFormSurveyStatistics> getStatistics(WeFormSurveyStatistics query);

    /**
     * 获取日期范围内统计的数据
     *
     * @param query
     * @return {@link List< WeFormSurveyStatistics>}
     * @author WangYX
     * @date 2022/10/14 16:18
     */
    List<WeFormSurveyStatistics> dataList(WeFormSurveyStatisticQuery query);
}
