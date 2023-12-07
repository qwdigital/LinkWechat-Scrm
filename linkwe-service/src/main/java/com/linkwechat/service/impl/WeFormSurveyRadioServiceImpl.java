package com.linkwechat.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeFormSurveyRadio;
import com.linkwechat.domain.form.query.WeFormSurveyRadioQuery;
import com.linkwechat.mapper.WeFormSurveyRadioMapper;
import com.linkwechat.service.IWeFormSurveyRadioService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 问卷-单选、多选题-选项(WeFormSurveyRadio)
 *
 * @author danmo
 * @since 2022-09-20 18:02:57
 */
@Service
public class WeFormSurveyRadioServiceImpl extends ServiceImpl<WeFormSurveyRadioMapper, WeFormSurveyRadio> implements IWeFormSurveyRadioService {


    @Override
    public List<WeFormSurveyRadio> selectNumber(WeFormSurveyRadioQuery query) {
        QueryWrapper<WeFormSurveyRadio> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT question_number,label,options");
        if (StringUtils.isNotBlank(query.getDataSource())){
            queryWrapper.eq("data_source",query.getDataSource());
        }
        if (query.getStartDate() != null) {
            queryWrapper.apply("date_form(create_time, '%Y-%m-%d') >= '"+ DateUtil.formatDate(query.getStartDate()) +"'");
        }
        if (query.getEndDate() != null){
            queryWrapper.apply("date_form(create_time, '%Y-%m-%d') <= '"+ DateUtil.formatDate(query.getEndDate()) +"'");
        }
        if (StringUtils.isNotBlank(query.getQuestionNumber())){
            queryWrapper.eq("question_number",query.getQuestionNumber());
        }
        if (StringUtils.isNotBlank(query.getLabel()) && query.getLabel().equals("pie")){
            queryWrapper.eq("form_id",query.getFormId()).apply("form_code_id != 9");
        }
        if (StringUtils.isNotBlank(query.getLabel()) && query.getLabel().equals("area")){
            queryWrapper.eq("form_id",query.getFormId()).apply("form_code_id = 9");
        }
        return list(queryWrapper);
    }

    @Override
    public List<WeFormSurveyRadio> selectDefaultValue(WeFormSurveyRadioQuery query) {
        QueryWrapper<WeFormSurveyRadio> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT form_code_id, default_value");
        if (StringUtils.isNotBlank(query.getDataSource())){
            queryWrapper.eq("data_source",query.getDataSource());
        }
        if (query.getStartDate() != null) {
            queryWrapper.apply("create_time >= '"+ DateUtil.formatDate(query.getStartDate()) +"'");
        }
        if (query.getEndDate() != null){
            queryWrapper.apply("create_time <= '"+ DateUtil.formatDate(query.getEndDate()) +"'");
        }
        if (StringUtils.isNotBlank(query.getQuestionNumber())){
            queryWrapper.eq("question_number",query.getQuestionNumber());
        }
        if (StringUtils.isNotBlank(query.getLabel()) && query.getLabel().equals("pie")){
            queryWrapper.eq("form_id",query.getFormId()).apply("form_code_id != 9");
        }
        if (StringUtils.isNotBlank(query.getLabel()) && query.getLabel().equals("area")){
            queryWrapper.eq("form_id",query.getFormId()).apply("form_code_id = 9");
        }
        return list(queryWrapper);
    }

    @Override
    public Integer countDefaultValue(WeFormSurveyRadioQuery query) {
        QueryWrapper<WeFormSurveyRadio> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(query.getDataSource())){
            queryWrapper.eq("data_source",query.getDataSource());
        }
        if (query.getStartDate() != null) {
            queryWrapper.apply("create_time >= '"+ DateUtil.formatDate(query.getStartDate()) +"'");
        }
        if (query.getEndDate() != null){
            queryWrapper.apply("create_time <= '"+ DateUtil.formatDate(query.getEndDate()) +"'");
        }
        if (StringUtils.isNotBlank(query.getQuestionNumber())){
            queryWrapper.eq("question_number",query.getQuestionNumber());
        }
        if (StringUtils.isNotBlank(query.getDefaultValue())){
            queryWrapper.eq("default_value",query.getDefaultValue());
        }
        if (StringUtils.isNotBlank(query.getLabel()) && query.getLabel().equals("pie")){
            queryWrapper.eq("form_id",query.getFormId()).apply("form_code_id != 9");
        }
        if (StringUtils.isNotBlank(query.getLabel()) && query.getLabel().equals("area")){
            queryWrapper.eq("form_id",query.getFormId()).apply("form_code_id = 9");
        }
        return count(queryWrapper);
    }

    @Override
    public Integer selectCountArea(WeFormSurveyRadioQuery query) {
        QueryWrapper<WeFormSurveyRadio> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(query.getDataSource())){
            queryWrapper.eq("data_source",query.getDataSource());
        }
        if (query.getStartDate() != null) {
            queryWrapper.apply("create_time >= '"+ DateUtil.formatDate(query.getStartDate()) +"'");
        }
        if (query.getEndDate() != null){
            queryWrapper.apply("create_time <= '"+ DateUtil.formatDate(query.getEndDate()) +"'");
        }
        if (StringUtils.isNotBlank(query.getQuestionNumber())){
            queryWrapper.eq("question_number",query.getQuestionNumber());
        }
        if (StringUtils.isNotBlank(query.getLabel()) && query.getLabel().equals("pie")){
            queryWrapper.eq("form_id",query.getFormId()).apply("form_code_id != 9");
        }
        if (StringUtils.isNotBlank(query.getLabel()) && query.getLabel().equals("area")){
            queryWrapper.eq("form_id",query.getFormId()).apply("form_code_id = 9");
        }
        queryWrapper.apply("FIND_IN_SET ('"+query.getCode()+"',default_value)");
        return count(queryWrapper);
    }
}
