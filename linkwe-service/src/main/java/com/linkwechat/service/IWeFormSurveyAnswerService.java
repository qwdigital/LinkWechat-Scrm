package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeFormSurveyAnswer;
import com.linkwechat.domain.form.query.WeAddFormSurveyAnswerQuery;
import com.linkwechat.domain.form.query.WeFormSurveyAnswerQuery;
import com.linkwechat.domain.form.query.WeFormSurveyStatisticQuery;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 答题-用户主表(WeFormSurveyAnswer)
 *
 * @author danmo
 * @since 2022-09-20 18:02:56
 */
public interface IWeFormSurveyAnswerService extends IService<WeFormSurveyAnswer> {

    /**
     * 新增答题
     *
     * @param query
     */
    void addAnswer(WeAddFormSurveyAnswerQuery query);

    /**
     * 查询答题列表
     *
     * @param query
     * @return
     */
    List<WeFormSurveyAnswer> getAnswerList(WeFormSurveyAnswerQuery query);

    /**
     * 判断用户是否已经填写表单
     *
     * @param query
     * @return
     */
    Integer isCompleteSurvey(WeFormSurveyAnswerQuery query);

    List<WeFormSurveyAnswer> selectCustomerList(WeFormSurveyStatisticQuery query);


}
