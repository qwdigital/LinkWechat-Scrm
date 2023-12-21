package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeFormSurveyAnswer;
import com.linkwechat.domain.WeFormSurveyCatalogue;
import com.linkwechat.domain.form.query.WeAddFormSurveyAnswerQuery;
import com.linkwechat.domain.form.query.WeFormSurveyAnswerQuery;
import com.linkwechat.domain.form.query.WeFormSurveyStatisticQuery;
import com.linkwechat.mapper.WeFormSurveyAnswerMapper;
import com.linkwechat.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 答题-用户主表(WeFormSurveyAnswer)
 *
 * @author danmo
 * @since 2022-09-20 18:02:56
 */
@Service
public class WeFormSurveyAnswerServiceImpl extends ServiceImpl<WeFormSurveyAnswerMapper, WeFormSurveyAnswer> implements IWeFormSurveyAnswerService {


    @Autowired
    private IWeFormSurveyCatalogueService weFormSurveyCatalogueService;


    @Autowired
    private IWeFormSurveyCountService iWeFormSurveyCountService;


    @Autowired
    private IWeCustomerService weCustomerService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAnswer(WeAddFormSurveyAnswerQuery query) {
        WeFormSurveyCatalogue formSurveyCatalogue = weFormSurveyCatalogueService.getWeFormSurveyCatalogueById(query.getBelongId());
        Integer rules = formSurveyCatalogue.getFillingRules();
        if(Objects.nonNull(rules) && Objects.equals(0,rules)){
            int count = count(new LambdaQueryWrapper<WeFormSurveyAnswer>()
                    .eq(WeFormSurveyAnswer::getBelongId, query.getBelongId())
                    .eq(WeFormSurveyAnswer::getUnionId, query.getUnionId())
                    .eq(WeFormSurveyAnswer::getDataSource, query.getDataSource())
                    .eq(WeFormSurveyAnswer::getAnEffective, 0)
                    .eq(WeFormSurveyAnswer::getDelFlag, 0));
            if (count > 0) {
                throw new WeComException("该用户已填写");
            }
        }
        if (Objects.nonNull(rules) && Objects.equals(1,rules)) {
            int count = count(new LambdaQueryWrapper<WeFormSurveyAnswer>()
                    .eq(WeFormSurveyAnswer::getBelongId, query.getBelongId())
                    .eq(WeFormSurveyAnswer::getUnionId, query.getUnionId())
                    .eq(WeFormSurveyAnswer::getDataSource, query.getDataSource())
                    .eq(WeFormSurveyAnswer::getAnEffective, 0)
                    .eq(WeFormSurveyAnswer::getDelFlag, 0)
                    .apply("date_format (create_time,'%Y-%m-%d') = '" + DateUtil.today() + "'")
            );
            if (count > 0) {
                throw new WeComException("该用户今天已填写");
            }
        }
        WeFormSurveyAnswer weFormSurveyAnswer = new WeFormSurveyAnswer();
        BeanUtil.copyProperties(query, weFormSurveyAnswer);
        weFormSurveyAnswer.setIpAddr(query.getIpAddr());
        if(save(weFormSurveyAnswer)){
            //设置时长
            iWeFormSurveyCountService.setVisitTime(query.getBelongId(),query.getIpAddr(),query.getDataSource()
                    ,DateUtils.timeDifference(new Date(),query.getAnTime()));
        }

    }

    @Override
    public List<WeFormSurveyAnswer> getAnswerList(WeFormSurveyAnswerQuery query) {
        LambdaQueryWrapper<WeFormSurveyAnswer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeFormSurveyAnswer::getBelongId, query.getBelongId());
        queryWrapper.eq(WeFormSurveyAnswer::getAnEffective, 0);
        queryWrapper.eq(StringUtils.isNotBlank(query.getDataSource()), WeFormSurveyAnswer::getDataSource, query.getDataSource());
        queryWrapper.apply(Objects.nonNull(query.getStartDate()), "DATE_FORMAT(CREATE_TIME, '%Y-%m-%d' ) >= '" + DateUtil.formatDate(query.getStartDate()) + "'");
        queryWrapper.apply(Objects.nonNull(query.getEndDate()), "DATE_FORMAT(CREATE_TIME, '%Y-%m-%d' ) <= '" + DateUtil.formatDate(query.getEndDate()) + "'");
        return list(queryWrapper);
    }

    @Override
    public Integer isCompleteSurvey(WeFormSurveyAnswerQuery query) {
        //获取表单信息
        WeFormSurveyCatalogue weFormSurveyCatalogue = weFormSurveyCatalogueService.getInfo(query.getBelongId(),null,null,false);
        //判断表单填写规则
        Integer fillingRules = weFormSurveyCatalogue.getFillingRules();
        if (fillingRules.equals(2)) {
            //不限制填写次数
            return 0;
        }
        LambdaQueryWrapper<WeFormSurveyAnswer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeFormSurveyAnswer::getBelongId, query.getBelongId());
        queryWrapper.eq(WeFormSurveyAnswer::getOpenId, query.getOpenId());
        queryWrapper.eq(WeFormSurveyAnswer::getDataSource, query.getDataSource());
        queryWrapper.eq(WeFormSurveyAnswer::getAnEffective, 0);
        queryWrapper.eq(WeFormSurveyAnswer::getDelFlag, 0);
        if (fillingRules.equals(0)) {
            //每人填写一次
            int count = count(queryWrapper);
            if (count > 0) {
                throw new WeComException("该用户已填写");
            }
        } else if (fillingRules.equals(1)) {
            //每人每天填写一次
            queryWrapper.apply("date_format (create_time,'%Y-%m-%d') = '" + DateUtil.today() + "'");
            int count = count(queryWrapper);
            if (count > 0) {
                throw new WeComException("该用户今天已填写");
            }
        }
        return 0;
    }

    @Override
    public List<WeFormSurveyAnswer> selectCustomerList(WeFormSurveyStatisticQuery query) {
        LambdaQueryWrapper<WeFormSurveyAnswer> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(WeFormSurveyAnswer::getBelongId, query.getBelongId());
        queryWrapper.eq(StringUtils.isNotBlank(query.getDataSource()), WeFormSurveyAnswer::getDataSource, query.getDataSource());
        queryWrapper.apply(StringUtils.isNotEmpty(query.getBeginTime())&&StringUtils.isNotEmpty(query.getEndTime()),"date_format(CREATE_TIME,'%Y-%m-%d') BETWEEN '"+query.getBeginTime()+"' and '"+query.getEndTime()+ "'" );
        queryWrapper.orderByDesc(WeFormSurveyAnswer::getCreateTime);
        List<WeFormSurveyAnswer> resultList = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(resultList)) {
            List<String> unionIds = resultList.stream().map(WeFormSurveyAnswer::getUnionId).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(unionIds)){
                List<WeCustomer> weCustomers = weCustomerService.list(new LambdaQueryWrapper<WeCustomer>().select(WeCustomer::getId, WeCustomer::getUnionid).in(WeCustomer::getUnionid, unionIds).eq(WeCustomer::getDelFlag, 0));

                Map<String, Long> unionIdAndCountMap = new HashMap<>(16);
                if (CollectionUtil.isNotEmpty(weCustomers)) {
                    unionIdAndCountMap = weCustomers.stream().collect(Collectors.groupingBy(WeCustomer::getUnionid, Collectors.counting()));
                }
                for (WeFormSurveyAnswer list : resultList) {
                    if (unionIdAndCountMap.containsKey(list.getUnionId())) {
                        list.setIsOfficeCustomer(true);
                    } else {
                        list.setIsOfficeCustomer(false);
                    }
                }
            }
        }
        return resultList;
    }


}
