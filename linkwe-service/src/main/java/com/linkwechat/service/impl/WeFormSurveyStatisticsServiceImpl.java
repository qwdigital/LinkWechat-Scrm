package com.linkwechat.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.SiteStatsConstants;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.*;
import com.linkwechat.domain.form.query.WeFormSurveyStatisticQuery;
import com.linkwechat.mapper.WeFormSurveySiteStasMapper;
import com.linkwechat.mapper.WeFormSurveyStatisticsMapper;
import com.linkwechat.service.IWeFormSurveyAnswerService;
import com.linkwechat.service.IWeFormSurveyCatalogueService;
import com.linkwechat.service.IWeFormSurveyCountService;
import com.linkwechat.service.IWeFormSurveyStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 * 问卷-统计表(WeFormSurveyStatistics)
 *
 * @author danmo
 * @since 2022-09-20 18:02:57
 */
@Service
public class WeFormSurveyStatisticsServiceImpl extends ServiceImpl<WeFormSurveyStatisticsMapper, WeFormSurveyStatistics> implements IWeFormSurveyStatisticsService {

//    @Lazy
//    @Autowired
//    private IWeFormSurveyAnswerService weFormSurveyAnswerService;
//
//    @Lazy
//    @Resource
//    private IWeFormSurveyCatalogueService weFormSurveyCatalogueService;
//    @Resource
//    private RedisTemplate redisTemplate;
//    @Resource
//    private WeFormSurveySiteStasMapper weFormSurveySiteStasMapper;


    @Autowired
    private IWeFormSurveyCountService iWeFormSurveyCountService;

    @Override
    public void delStatistics(WeFormSurveyStatistics surveyStatistics) {
        LambdaUpdateWrapper<WeFormSurveyStatistics> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.set(WeFormSurveyStatistics::getDelFlag, 1);
        queryWrapper.eq(WeFormSurveyStatistics::getBelongId, surveyStatistics.getBelongId())
                .apply("date_format (create_time,'%Y-%m-%d') = " + "'" + DateUtil.today() + "'");
        update(queryWrapper);
    }

    @Override
    public List<WeFormSurveyStatistics> getStatistics(WeFormSurveyStatistics query) {
        WeFormSurveyStatistics weFormSurveyStatistics=new WeFormSurveyStatistics();

        WeFormSurveyCount weFormSurveyCount = WeFormSurveyCount.builder()
                .belongId(query.getBelongId())
                .build();

        weFormSurveyCount.setBeginTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,new Date()));
        weFormSurveyCount.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,new Date()));
        WeFormSurveyStatistics nowStatistics = iWeFormSurveyCountService.getStatistics(weFormSurveyCount);
        if(null != nowStatistics){
            weFormSurveyStatistics=nowStatistics;
        }

        weFormSurveyCount.setBeginTime(DateUtils.getYesterday(DateUtils.YYYY_MM_DD));
        weFormSurveyCount.setEndTime(DateUtils.getYesterday(DateUtils.YYYY_MM_DD));
        WeFormSurveyStatistics yesterStatistics = iWeFormSurveyCountService.getStatistics(weFormSurveyCount);
        if(null != yesterStatistics){
            weFormSurveyStatistics.setYesTotalVisits(yesterStatistics.getYesTotalVisits());
            weFormSurveyStatistics.setYesTotalUser(yesterStatistics.getYesTotalUser());
            weFormSurveyStatistics.setYesCollectionVolume(yesterStatistics.getYesCollectionVolume());
        }


        List<WeFormSurveyStatistics> result = new ArrayList<>();
        result.add(weFormSurveyStatistics);
        return result;
    }

    @Override
    public List<WeFormSurveyStatistics> dataList(WeFormSurveyStatisticQuery query) {
        WeFormSurveyCount weFormSurveyCount = WeFormSurveyCount.builder()
                .belongId(query.getBelongId())
                .channelsName(query.getDataSource())
                .build();
        weFormSurveyCount.setBeginTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,query.getStartDate()));
        weFormSurveyCount.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,query.getEndDate()));
        return iWeFormSurveyCountService.findDataList(weFormSurveyCount);
    }
}
