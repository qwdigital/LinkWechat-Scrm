package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeFormSurveyRadio;
import com.linkwechat.domain.form.query.WeFormSurveyRadioQuery;

import java.util.List;

/**
 * 问卷-单选、多选题-选项(WeFormSurveyRadio)
 *
 * @author danmo
 * @since 2022-09-20 18:02:57
 */
public interface IWeFormSurveyRadioService extends IService<WeFormSurveyRadio> {

    List<WeFormSurveyRadio> selectNumber(WeFormSurveyRadioQuery query);

    List<WeFormSurveyRadio> selectDefaultValue(WeFormSurveyRadioQuery query);

    Integer countDefaultValue(WeFormSurveyRadioQuery newTQuRadio);

    Integer selectCountArea(WeFormSurveyRadioQuery query);
}
