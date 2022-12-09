package com.linkwechat.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.SiteStatsConstants;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeFormSurveyAnswer;
import com.linkwechat.domain.WeFormSurveyCatalogue;
import com.linkwechat.domain.WeFormSurveySiteStas;
import com.linkwechat.domain.WeFormSurveyStatistics;
import com.linkwechat.domain.form.query.WeFormSurveyStatisticQuery;
import com.linkwechat.mapper.WeFormSurveySiteStasMapper;
import com.linkwechat.mapper.WeFormSurveyStatisticsMapper;
import com.linkwechat.service.IWeFormSurveyAnswerService;
import com.linkwechat.service.IWeFormSurveyCatalogueService;
import com.linkwechat.service.IWeFormSurveyStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.ArrayList;
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

    @Lazy
    @Autowired
    private IWeFormSurveyAnswerService weFormSurveyAnswerService;

    @Lazy
    @Resource
    private IWeFormSurveyCatalogueService weFormSurveyCatalogueService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private WeFormSurveySiteStasMapper weFormSurveySiteStasMapper;

    @Override
    public void delStatistics(WeFormSurveyStatistics surveyStatistics) {
        LambdaUpdateWrapper<WeFormSurveyStatistics> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.set(WeFormSurveyStatistics::getDelFlag, 1);
        queryWrapper.eq(WeFormSurveyStatistics::getBelongId, surveyStatistics.getBelongId())
                .apply("date_format (create_time,'%Y-%m-%d') = " + "'" + DateUtil.today() + "'");
        update(queryWrapper);
    }

    @Override
    public void addStatistics(WeFormSurveyStatistics surveyStatistics) {
        LambdaQueryWrapper<WeFormSurveyAnswer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeFormSurveyAnswer::getBelongId, surveyStatistics.getBelongId());
        queryWrapper.eq(WeFormSurveyAnswer::getDelFlag, 0);
        List<WeFormSurveyAnswer> answerList = weFormSurveyAnswerService.list(queryWrapper);
        //总访问量
        surveyStatistics.setTotalVisits(answerList.size());
        //有效收集量
        Long collectionVolume = answerList.parallelStream().filter(answer -> Objects.equals(0, answer.getAnEffective())).count();
        surveyStatistics.setCollectionVolume(collectionVolume.intValue());
        //总访问用户
        Long realUser = answerList.parallelStream().filter(answer -> Objects.equals(0, answer.getAnEffective())).map(WeFormSurveyAnswer::getMobile).distinct().count();
        Long errUser = answerList.parallelStream().filter(answer -> Objects.equals(0, answer.getAnEffective())).map(WeFormSurveyAnswer::getIpAddr).distinct().count();
        int totalUser = 0;
        List<WeFormSurveyAnswer> ipAddr = answerList.parallelStream()
                .filter(answer -> Objects.equals(surveyStatistics.getCollectionRate(), answer.getIpAddr())
                        && Objects.equals(surveyStatistics.getDataSource(), answer.getDataSource())).collect(Collectors.toList());
        if (ipAddr.size() > 0) {
            totalUser = realUser.intValue() + errUser.intValue() - 1;
        } else {
            totalUser = realUser.intValue() + errUser.intValue();

        }
        surveyStatistics.setTotalUser(totalUser);
        //收集率
        double decimal = (float) collectionVolume / (float) totalUser;
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(2);
        String collectionRate = nt.format(decimal);
        surveyStatistics.setCollectionRate(collectionRate);
        //平均完成时间
        List<WeFormSurveyAnswer> averageTimeVolumes = answerList.parallelStream()
                .filter(answer -> Objects.equals(0, answer.getAnEffective())
                        && Objects.equals(surveyStatistics.getDataSource(), answer.getDataSource())
                        && StringUtils.isNotEmpty(answer.getMobile())).collect(Collectors.toList());
        float sum = 0;
        for (WeFormSurveyAnswer averageTimeVolume : averageTimeVolumes) {
            Float totalTime = averageTimeVolume.getTotalTime();
            sum = sum + totalTime;
        }
        float averageTime = sum / averageTimeVolumes.size();
        surveyStatistics.setAverageTime((int) averageTime);
        //较昨日
        LambdaQueryWrapper<WeFormSurveyStatistics> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(surveyStatistics.getDataSource()), WeFormSurveyStatistics::getDataSource, surveyStatistics.getDataSource());
        wrapper.eq(WeFormSurveyStatistics::getBelongId, surveyStatistics.getBelongId())
                .apply("date_format (create_time,'%Y-%m-%d') = " + "'" + DateUtil.yesterday().toDateStr() + "'");
        WeFormSurveyStatistics YesToday = getOne(wrapper);
        int yesCollectionVolume = 0;
        int yesTotalUser = 0;
        int yesTotalVisits = 0;
        if (YesToday == null) {
            surveyStatistics.setYesCollectionVolume(yesCollectionVolume);
            surveyStatistics.setYesTotalUser(yesTotalUser);
            surveyStatistics.setYesTotalVisits(yesTotalVisits);
        } else {
            yesCollectionVolume = collectionVolume.intValue() - YesToday.getCollectionVolume();
            surveyStatistics.setYesCollectionVolume(yesCollectionVolume);
            yesTotalUser = totalUser - YesToday.getTotalUser();
            surveyStatistics.setYesTotalUser(yesTotalUser);
            yesTotalVisits = surveyStatistics.getTotalVisits() - YesToday.getTotalVisits();
            surveyStatistics.setYesTotalVisits(yesTotalVisits);
        }
        surveyStatistics.setDataSource(surveyStatistics.getDataSource());
        save(surveyStatistics);
    }

    @Override
    public List<WeFormSurveyStatistics> getStatistics(WeFormSurveyStatistics query) {
        Long belongId = query.getBelongId();
        WeFormSurveyCatalogue weFormSurveyCatalogue = weFormSurveyCatalogueService.getById(belongId);
        String channelsName = weFormSurveyCatalogue.getChannelsName();

        Integer pv = 0;
        Long uv = 0L;
        String[] split = channelsName.split(",");
        for (String channelName : split) {
            //PV
            String pvKey = StringUtils.format(SiteStatsConstants.PREFIX_KEY_PV, belongId, channelName);
            Object o = redisTemplate.opsForValue().get(pvKey);
            if(Objects.nonNull(o)){
                pv += (Integer)o;
            }


            String ipKey = StringUtils.format(SiteStatsConstants.PREFIX_KEY_IP, belongId, channelName);
            Long size = redisTemplate.opsForSet().size(ipKey);
            if(null != size){
                uv += size;
            }

        }
        uv = Long.valueOf(uv.intValue() - split.length);

        WeFormSurveyStatistics weFormSurveyStatistics = new WeFormSurveyStatistics();
        weFormSurveyStatistics.setBelongId(belongId);
        //总访问量
        weFormSurveyStatistics.setTotalVisits(pv);
        //总访问用户量
        weFormSurveyStatistics.setTotalUser(uv.intValue()<0?0:uv.intValue());

        //有效的收集量
        QueryWrapper<WeFormSurveyAnswer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WeFormSurveyAnswer::getBelongId, belongId);
        queryWrapper.lambda().eq(WeFormSurveyAnswer::getAnEffective, 0);
        queryWrapper.lambda().eq(WeFormSurveyAnswer::getDelFlag, Constants.NORMAL_CODE);
        List<WeFormSurveyAnswer> list = weFormSurveyAnswerService.list(queryWrapper);

        //有效收集量
        weFormSurveyStatistics.setCollectionVolume(list.size());
        //收集率
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        percentInstance.setMaximumFractionDigits(2);
        if (list != null && list.size() > 0 && uv > 0) {
            double v = Double.valueOf(list.size()) / Double.valueOf(uv);
            weFormSurveyStatistics.setCollectionRate(percentInstance.format(v));
        } else {
            weFormSurveyStatistics.setCollectionRate(percentInstance.format(0));
        }

        //平均完成时间
        if (list != null && list.size() > 0) {
            double sum = list.stream().flatMapToDouble(item -> DoubleStream.of(item.getTotalTime())).sum();
            Double averageTime = sum / 1000 / list.size();
            weFormSurveyStatistics.setAverageTime(averageTime.intValue());
        } else {
            weFormSurveyStatistics.setAverageTime(0);
        }


        //上次的站点统计的数据(也就是昨天站点统计的数据)
        QueryWrapper<WeFormSurveySiteStas> siteStasQueryWrapper = new QueryWrapper<>();
        siteStasQueryWrapper.lambda().eq(WeFormSurveySiteStas::getBelongId, weFormSurveyCatalogue.getId());
        WeFormSurveySiteStas weFormSurveySiteStas = weFormSurveySiteStasMapper.selectOne(siteStasQueryWrapper);

        //较昨日总访问量
        int yesTotalVisits = pv - (weFormSurveySiteStas != null ? weFormSurveySiteStas.getTotalVisits() : 0);
        weFormSurveyStatistics.setYesTotalVisits(yesTotalVisits);
        //较昨日总访问用户量
        int yesTotalUser = uv.intValue() - (weFormSurveySiteStas != null ? weFormSurveySiteStas.getTotalUser() : 0);
        weFormSurveyStatistics.setYesTotalUser(yesTotalUser);
        //较昨日有效收集量
        int size = list != null ? list.size() : 0;
        int yesCollectionVolume = size - (weFormSurveySiteStas != null ? weFormSurveySiteStas.getCollectionVolume() : 0);
        weFormSurveyStatistics.setYesCollectionVolume(yesCollectionVolume);

        List<WeFormSurveyStatistics> result = new ArrayList<>();
        result.add(weFormSurveyStatistics);
        return result;
    }

    @Override
    public List<WeFormSurveyStatistics> dataList(WeFormSurveyStatisticQuery query) {
        LambdaQueryWrapper<WeFormSurveyStatistics> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeFormSurveyStatistics::getBelongId, query.getBelongId());
        queryWrapper.eq(StringUtils.isNotBlank(query.getDataSource()), WeFormSurveyStatistics::getDataSource, query.getDataSource());
        queryWrapper.apply(Objects.nonNull(query.getStartDate()), "DATE_FORMAT(CREATE_TIME, '%Y-%m-%d' ) >= '" + DateUtil.formatDate(query.getStartDate()) + "'");
        queryWrapper.apply(Objects.nonNull(query.getEndDate()), "DATE_FORMAT(CREATE_TIME, '%Y-%m-%d' ) <= '" + DateUtil.formatDate(query.getEndDate()) + "'");
        queryWrapper.orderByDesc(BaseEntity::getCreateTime);
        return list(queryWrapper);
    }
}
