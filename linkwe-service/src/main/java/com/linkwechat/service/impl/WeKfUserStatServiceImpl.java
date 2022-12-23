package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeKfUserStat;
import com.linkwechat.domain.kf.query.WeKfQualityStatQuery;
import com.linkwechat.domain.kf.vo.WeKfQualityBrokenLineVo;
import com.linkwechat.domain.kf.vo.WeKfQualityChatVo;
import com.linkwechat.domain.kf.vo.WeKfQualityHistogramVo;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeKfUserStatMapper;
import com.linkwechat.service.IWeKfUserStatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 客服员工统计表(WeKfUserStat)
 *
 * @author danmo
 * @since 2022-11-28 16:48:24
 */
@Service
public class WeKfUserStatServiceImpl extends ServiceImpl<WeKfUserStatMapper, WeKfUserStat> implements IWeKfUserStatService {

    @Resource
    private QwSysUserClient qwSysUserClient;

    @Override
    public List<WeKfQualityBrokenLineVo> getQualityBrokenLine(WeKfQualityStatQuery query) {
        List<WeKfQualityBrokenLineVo> brokenLineList = new LinkedList<>();
        LambdaQueryWrapper<WeKfUserStat> wrapper = new LambdaQueryWrapper<>();
        if (CollectionUtil.isNotEmpty(query.getUserIds())) {
            wrapper.in(WeKfUserStat::getUserId, query.getUserIds());
        }
        if (CollectionUtil.isNotEmpty(query.getOpenKfIds())) {
            wrapper.in(WeKfUserStat::getOpenKfId, query.getOpenKfIds());
        }
        if (StringUtils.isEmpty(query.getBeginTime())) {
            query.setBeginTime(DateUtil.offsetDay(new Date(), -7).toDateStr());
        }
        wrapper.apply("str_to_date(date_time,'%Y-%m-%d') >= '" + query.getBeginTime() + "'");
        if (StringUtils.isEmpty(query.getEndTime())) {
            query.setEndTime(DateUtil.today());
        }
        wrapper.apply("str_to_date(date_time,'%Y-%m-%d') <= '" + query.getEndTime() + "'");

        List<WeKfUserStat> userStatList = list(wrapper.orderByAsc(WeKfUserStat::getDateTime));

        List<DateTime> dateTimeList = DateUtil.rangeToList(DateUtil.parseDate(query.getBeginTime()), DateUtil.parseDate(query.getEndTime()), DateField.DAY_OF_YEAR);
        for (DateTime dateTime : dateTimeList) {
            WeKfQualityBrokenLineVo brokenLine = new WeKfQualityBrokenLineVo();
            brokenLine.setDateTime(dateTime.toDateStr());
            List<WeKfUserStat> statList = Optional.ofNullable(userStatList).orElseGet(ArrayList::new)
                    .stream().filter(item -> Objects.equals(dateTime.toDateStr(), item.getDateTime())).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(statList)) {
                NumberFormat fmt = NumberFormat.getPercentInstance();
                //参评总数
                int evaluateCnt = statList.stream().mapToInt(WeKfUserStat::getEvaluateCnt).sum();
                brokenLine.setEvaluateCnt(evaluateCnt);
                //参评率
                int sessionCnt = statList.stream().mapToInt(WeKfUserStat::getSessionCnt).sum();
                //参评率
                float evaluateAsr = (float) evaluateCnt / sessionCnt;
                if (!Float.isNaN(evaluateAsr)) {
                    brokenLine.setEvaluateRate(fmt.format(evaluateAsr));
                }
                //好评率
                int goodCnt = statList.stream().mapToInt(WeKfUserStat::getGoodCnt).sum();
                float goodAsr = (float) goodCnt / evaluateCnt;
                if (!Float.isNaN(goodAsr)) {
                    brokenLine.setGoodRate(fmt.format(goodAsr));
                }
                //一般率
                int commonCnt = statList.stream().mapToInt(WeKfUserStat::getCommonCnt).sum();
                float commonAsr = (float) commonCnt / evaluateCnt;
                if (!Float.isNaN(commonAsr)) {
                    brokenLine.setCommonRate(fmt.format(commonAsr));
                }
                //差评率
                int badCnt = statList.stream().mapToInt(WeKfUserStat::getBadCnt).sum();
                float badAsr = (float) badCnt / evaluateCnt;
                if (!Float.isNaN(badAsr)) {
                    brokenLine.setBadRate(fmt.format(badAsr));
                }
            }
            brokenLineList.add(brokenLine);
        }
        return brokenLineList;
    }

    @Override
    public List<WeKfQualityHistogramVo> getQualityHistogram(WeKfQualityStatQuery query) {
        List<WeKfQualityHistogramVo> histogramList = new LinkedList<>();
        LambdaQueryWrapper<WeKfUserStat> wrapper = new LambdaQueryWrapper<>();
        if (CollectionUtil.isNotEmpty(query.getOpenKfIds())) {
            wrapper.in(WeKfUserStat::getOpenKfId, query.getOpenKfIds());
        }
        if (StringUtils.isEmpty(query.getBeginTime())) {
            query.setBeginTime(DateUtil.offsetDay(new Date(), -7).toDateStr());
        }
        wrapper.apply("str_to_date(date_time,'%Y-%m-%d') >= '" + query.getBeginTime() + "'");
        if (StringUtils.isEmpty(query.getEndTime())) {
            query.setEndTime(DateUtil.today());
        }
        wrapper.apply("str_to_date(date_time,'%Y-%m-%d') <= '" + query.getEndTime() + "'");

        List<WeKfUserStat> userStatList = list(wrapper);
        if (CollectionUtil.isNotEmpty(userStatList)) {
            Map<String, List<WeKfUserStat>> userStatMap = userStatList.stream().filter(item -> StringUtils.isNotEmpty(item.getUserId())).collect(Collectors.groupingBy(WeKfUserStat::getUserId));
            Set<String> userIds = userStatMap.keySet();
            SysUserQuery sysUserQuery = new SysUserQuery();
            sysUserQuery.setWeUserIds(new ArrayList<>(userIds));
            List<SysUserVo> sysUserList = qwSysUserClient.getUserListByWeUserIds(sysUserQuery).getData();
            Map<String, Set<String>> userId2NameMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(sysUserList)){
                Map<String, Set<String>> setMap = sysUserList.stream().collect(Collectors.groupingBy(SysUserVo::getOpenUserid, Collectors.mapping(SysUserVo::getUserName, Collectors.toSet())));
                userId2NameMap.putAll(setMap);
            }
            userStatMap.forEach((userId, statList) -> {
                WeKfQualityHistogramVo histogram = new WeKfQualityHistogramVo();
                if(CollectionUtil.isNotEmpty(userId2NameMap) && userId2NameMap.containsKey(userId)){
                    Set<String> nameSet = userId2NameMap.get(userId);
                    if(!nameSet.isEmpty()){
                        histogram.setUserName(nameSet.stream().findFirst().get());
                        histogram.setUserId(userId);
                    }
                }
                NumberFormat fmt = NumberFormat.getPercentInstance();
                //参评总数
                int evaluateCnt = statList.stream().mapToInt(WeKfUserStat::getEvaluateCnt).sum();
                histogram.setEvaluateCnt(evaluateCnt);
                //参评率
                int sessionCnt = statList.stream().mapToInt(WeKfUserStat::getSessionCnt).sum();
                //参评率
                float evaluateAsr = (float) evaluateCnt / sessionCnt;
                if (!Float.isNaN(evaluateAsr)) {
                    histogram.setEvaluateRate(fmt.format(evaluateAsr));
                }
                //好评率
                int goodCnt = statList.stream().mapToInt(WeKfUserStat::getGoodCnt).sum();
                float goodAsr = (float) goodCnt / evaluateCnt;
                if (!Float.isNaN(goodAsr)) {
                    histogram.setGoodRate(fmt.format(goodAsr));
                }
                //一般率
                int commonCnt = statList.stream().mapToInt(WeKfUserStat::getCommonCnt).sum();
                float commonAsr = (float) commonCnt / evaluateCnt;
                if (!Float.isNaN(commonAsr)) {
                    histogram.setCommonRate(fmt.format(commonAsr));
                }
                //差评率
                int badCnt = statList.stream().mapToInt(WeKfUserStat::getBadCnt).sum();
                float badAsr = (float) badCnt / evaluateCnt;
                if (!Float.isNaN(badAsr)) {
                    histogram.setBadRate(fmt.format(badAsr));
                }
                histogramList.add(histogram);
            });
        }
        if (CollectionUtil.isNotEmpty(histogramList)) {
            if (Objects.equals(1, query.getType())) {
                List<WeKfQualityHistogramVo> collect = histogramList.stream().sorted(Comparator.comparing(item -> Double.valueOf(item.getEvaluateRate().replace("%", "")))).limit(5).collect(Collectors.toList());
                Collections.reverse(collect);
                return collect;
            } else if (Objects.equals(2, query.getType())) {
                List<WeKfQualityHistogramVo> collect = histogramList.stream().sorted(Comparator.comparing(item -> Double.valueOf(item.getGoodRate().replace("%", "")))).limit(5).collect(Collectors.toList());
                Collections.reverse(collect);
                return collect;
            } else if (Objects.equals(3, query.getType())) {
                List<WeKfQualityHistogramVo> collect = histogramList.stream().sorted(Comparator.comparing(item -> Double.valueOf(item.getCommonRate().replace("%","")))).limit(5).collect(Collectors.toList());
                Collections.reverse(collect);
                return collect;
            } else if (Objects.equals(4, query.getType())) {
                List<WeKfQualityHistogramVo> collect = histogramList.stream().sorted(Comparator.comparing(item -> Double.valueOf(item.getBadRate().replace("%","")))).limit(5).collect(Collectors.toList());
                Collections.reverse(collect);
                return collect;
            }
        }
        return histogramList;
    }

    @Override
    public PageInfo<WeKfQualityChatVo> getQualityChart(WeKfQualityStatQuery query) {
        List<WeKfQualityChatVo> chatList = new LinkedList<>();
        if (StringUtils.isEmpty(query.getBeginTime())) {
            query.setBeginTime(DateUtil.offsetDay(new Date(), -7).toDateStr());
        }
        if (StringUtils.isEmpty(query.getEndTime())) {
            query.setEndTime(DateUtil.today());
        }
        List<WeKfUserStat> userStatList = this.baseMapper.getQualityChart(query);

        if (CollectionUtil.isNotEmpty(userStatList)) {
            for (WeKfUserStat kfUserStat : userStatList) {
                WeKfQualityChatVo chart = new WeKfQualityChatVo();
                chart.setDateTime(kfUserStat.getDateTime());
                NumberFormat fmt = NumberFormat.getPercentInstance();
                //参评总数
                int evaluateCnt = kfUserStat.getEvaluateCnt();
                chart.setEvaluateCnt(evaluateCnt);
                //参评率
                int sessionCnt = kfUserStat.getSessionCnt();
                //参评率
                float evaluateAsr = (float) evaluateCnt / sessionCnt;
                if (!Float.isNaN(evaluateAsr)) {
                    chart.setEvaluateRate(fmt.format(evaluateAsr));
                }
                //好评率
                int goodCnt = kfUserStat.getGoodCnt();
                float goodAsr = (float) goodCnt / evaluateCnt;
                if (!Float.isNaN(goodAsr)) {
                    chart.setGoodRate(fmt.format(goodAsr));
                }
                //一般率
                int commonCnt = kfUserStat.getCommonCnt();
                float commonAsr = (float) commonCnt / evaluateCnt;
                if (!Float.isNaN(commonAsr)) {
                    chart.setCommonRate(fmt.format(commonAsr));
                }
                //差评率
                int badCnt = kfUserStat.getBadCnt();
                float badAsr = (float) badCnt / evaluateCnt;
                if (!Float.isNaN(badAsr)) {
                    chart.setBadRate(fmt.format(badAsr));
                }

                //超时率
                int talkCnt = kfUserStat.getTalkCnt();
                int timeOutCnt = kfUserStat.getTimeOutCnt();
                float timeOutAsr = (float) timeOutCnt / talkCnt;
                if (!Float.isNaN(timeOutAsr)) {
                    chart.setTimeOutRate(fmt.format(timeOutAsr));
                }
                //超时时长
                long timeOutDuration = kfUserStat.getTimeOutDuration();
                float timeOutDurationAsr = (float) timeOutDuration / timeOutCnt;
                if (!Float.isNaN(timeOutDurationAsr)) {
                    chart.setLong2TimeOutDuration(BigDecimal.valueOf(timeOutDurationAsr).longValue());
                }
                chatList.add(chart);
            }
        }

        PageInfo<WeKfUserStat> pageTempInfo = new PageInfo<>(userStatList);
        PageInfo<WeKfQualityChatVo> pageInfo = new PageInfo<>(chatList);
        pageInfo.setTotal(pageTempInfo.getTotal());
        pageInfo.setPageNum(pageTempInfo.getPageNum());
        pageInfo.setPageSize(pageTempInfo.getPageSize());
        return pageInfo;
    }

    public static void main(String[] args) {
        List<WeKfQualityHistogramVo> list  = new ArrayList<>();
        WeKfQualityHistogramVo histogramVo1 = new WeKfQualityHistogramVo();
        histogramVo1.setGoodRate("20%");
        list.add(histogramVo1);
        WeKfQualityHistogramVo histogramVo2 = new WeKfQualityHistogramVo();
        histogramVo2.setGoodRate("100%");
        list.add(histogramVo2);
        WeKfQualityHistogramVo histogramVo3 = new WeKfQualityHistogramVo();
        histogramVo3.setGoodRate("57%");
        list.add(histogramVo3);

        List<WeKfQualityHistogramVo> collect = list.stream().sorted(Comparator.comparing(item -> Double.valueOf(item.getGoodRate().replace("%","")))).limit(5).collect(Collectors.toList());
        Collections.reverse(collect);
        for (WeKfQualityHistogramVo histogramVo : collect) {
            System.out.println(histogramVo.getGoodRate());
        }
    }
}
