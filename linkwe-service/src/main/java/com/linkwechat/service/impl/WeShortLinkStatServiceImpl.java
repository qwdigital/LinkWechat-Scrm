package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.Base62NumUtil;
import com.linkwechat.domain.WeShortLinkStat;
import com.linkwechat.domain.shortlink.query.WeShortLinkStatisticQuery;
import com.linkwechat.domain.shortlink.vo.WeShortLinkStatisticsVo;
import com.linkwechat.mapper.WeShortLinkStatMapper;
import com.linkwechat.service.IWeShortLinkStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 短链统计表(WeShortLinkStat)
 *
 * @author danmo
 * @since 2023-01-10 23:04:10
 */
@Service
public class WeShortLinkStatServiceImpl extends ServiceImpl<WeShortLinkStatMapper, WeShortLinkStat> implements IWeShortLinkStatService {

    @Autowired
    private RedisService redisService;

    @Override
    public WeShortLinkStatisticsVo getDataStatistics(WeShortLinkStatisticQuery query) {
        WeShortLinkStatisticsVo statisticsVo = new WeShortLinkStatisticsVo();
        List<WeShortLinkStat> statList = list(new LambdaQueryWrapper<WeShortLinkStat>()
                .eq(Objects.nonNull(query.getId()), WeShortLinkStat::getShortId, query.getId()));

        List<WeShortLinkStat> yesterdayData = new LinkedList<>();
        if (CollectionUtil.isNotEmpty(statList)) {
            int pvNum = statList.stream().mapToInt(WeShortLinkStat::getPvNum).sum();
            statisticsVo.setPvTotalCount(pvNum);
            int uvNum = statList.stream().mapToInt(WeShortLinkStat::getUvNum).sum();
            statisticsVo.setUvTotalCount(uvNum);
            int openNum = statList.stream().mapToInt(WeShortLinkStat::getOpenNum).sum();
            statisticsVo.setOpenTotalCount(openNum);

            yesterdayData = statList.stream().filter(item -> ObjectUtil.equal(DateUtil.yesterday().toDateStr(), DateUtil.formatDate(item.getDateTime())))
                    .collect(Collectors.toList());
        }
        String shortUrl = "*";
        if(Objects.nonNull(query.getId())){
            shortUrl = Base62NumUtil.encode(query.getId());
        }

        //今日PV数
        Collection<String> pvKeys = redisService.keys(WeConstans.WE_SHORT_LINK_KEY + WeConstans.PV + shortUrl);
        Integer todayPvNum = 0;
        for (String pvKey : pvKeys) {
            Integer tpv = redisService.getCacheObject(pvKey);
            todayPvNum += tpv;
        }
        statisticsVo.setPvTotalCount(statisticsVo.getPvTotalCount() + todayPvNum);
        statisticsVo.setPvTodayCount(todayPvNum);
        int pvDiff = todayPvNum - yesterdayData.stream().mapToInt(WeShortLinkStat::getPvNum).sum();
        statisticsVo.setPvDiff(pvDiff);

        //今日UV数
        Collection<String> uvKeys = redisService.keys(WeConstans.WE_SHORT_LINK_KEY + WeConstans.UV + shortUrl);
        Integer todayUvNum = 0;
        for (String uvKey : uvKeys) {
            Long tuv = redisService.hyperLogLogCount(uvKey);
            todayUvNum += tuv.intValue();
        }
        statisticsVo.setUvTotalCount(statisticsVo.getUvTotalCount() + todayUvNum);
        statisticsVo.setUvTodayCount(todayUvNum);
        int uvDiff = todayUvNum - yesterdayData.stream().mapToInt(WeShortLinkStat::getUvNum).sum();
        statisticsVo.setUvDiff(uvDiff);

        //今日打开小程序数
        Collection<String> openKeys = redisService.keys(WeConstans.WE_SHORT_LINK_KEY + WeConstans.OPEN_APPLET + shortUrl);
        Integer todayOpenNum = 0;
        for (String openKey : openKeys) {
            Integer topen = redisService.getCacheObject(openKey);
            todayOpenNum += topen;
        }
        statisticsVo.setOpenTotalCount(statisticsVo.getOpenTotalCount() + todayOpenNum);
        statisticsVo.setOpenTodayCount(todayOpenNum);
        int openDiff = todayOpenNum - yesterdayData.stream().mapToInt(WeShortLinkStat::getOpenNum).sum();
        statisticsVo.setOpenDiff(openDiff);

        return statisticsVo;
    }

    @Override
    public WeShortLinkStatisticsVo getLineStatistics(WeShortLinkStatisticQuery query) {
        WeShortLinkStatisticsVo statisticsVo = new WeShortLinkStatisticsVo();
        List<WeShortLinkStat> statList = list(new LambdaQueryWrapper<WeShortLinkStat>()
                .eq(Objects.nonNull(query.getId()), WeShortLinkStat::getShortId, query.getId())
                .ge(Objects.nonNull(query.getBeginTime()), WeShortLinkStat::getDateTime, DateUtil.formatDate(query.getBeginTime()))
                .le(Objects.nonNull(query.getEndTime()), WeShortLinkStat::getDateTime, DateUtil.formatDate(query.getEndTime())));

        JSONObject yData = new JSONObject();
        List<Integer> pvList = new LinkedList<>();
        List<Integer> uvList = new LinkedList<>();
        List<Integer> openList = new LinkedList<>();
        List<DateTime> timeList = DateUtil.rangeToList(query.getBeginTime(), query.getEndTime(), DateField.DAY_OF_YEAR);
        List<String> dateList = timeList.stream().map(DateTime::toDateStr).collect(Collectors.toList());
        for (String dateTime : dateList) {
            int pv = 0;
            int uv = 0;
            int open = 0;
            //如果是今天取缓存数据
            if (ObjectUtil.equal(dateTime, DateUtil.formatDate(new Date()))) {
                if (Objects.nonNull(query.getId())) {
                    String shortUrl = Base62NumUtil.encode(query.getId());
                    //今日PV数
                    Collection<String> pvKeys = redisService.keys(WeConstans.WE_SHORT_LINK_KEY + WeConstans.PV + shortUrl);
                    Integer todayPvNum = 0;
                    for (String pvKey : pvKeys) {
                        Integer tpv = redisService.getCacheObject(pvKey);
                        pv += tpv;
                    }
                    //今日UV数
                    Collection<String> uvKeys = redisService.keys(WeConstans.WE_SHORT_LINK_KEY + WeConstans.UV + shortUrl);
                    for (String uvKey : uvKeys) {
                        Long tuv = redisService.hyperLogLogCount(uvKey);
                        uv += tuv.intValue();
                    }
                    //今日打开小程序数
                    Collection<String> openKeys = redisService.keys(WeConstans.WE_SHORT_LINK_KEY + WeConstans.OPEN_APPLET + shortUrl);
                    for (String openKey : openKeys) {
                        Integer topen = redisService.getCacheObject(openKey);
                        open += topen;
                    }
                } else {
                    //今日PV数
                    Collection<String> pvKeys = redisService.keys(WeConstans.WE_SHORT_LINK_KEY + WeConstans.PV + "*");
                    Integer todayPvNum = 0;
                    for (String pvKey : pvKeys) {
                        Integer tpv = redisService.getCacheObject(pvKey);
                        pv += tpv;
                    }
                    //今日UV数
                    Collection<String> uvKeys = redisService.keys(WeConstans.WE_SHORT_LINK_KEY + WeConstans.UV + "*");
                    for (String uvKey : uvKeys) {
                        Long tuv = redisService.hyperLogLogCount(uvKey);
                        uv += tuv.intValue();
                    }
                    //今日打开小程序数
                    Collection<String> openKeys = redisService.keys(WeConstans.WE_SHORT_LINK_KEY + WeConstans.OPEN_APPLET + "*");
                    for (String openKey : openKeys) {
                        Integer topen = redisService.getCacheObject(openKey);
                        open += topen;
                    }
                }

            } else {
                List<WeShortLinkStat> dateDataList = statList.stream().filter(item -> ObjectUtil.equal(dateTime, DateUtil.formatDate(item.getDateTime()))).collect(Collectors.toList());
                pv = dateDataList.stream().mapToInt(WeShortLinkStat::getPvNum).sum();
                uv = dateDataList.stream().mapToInt(WeShortLinkStat::getUvNum).sum();
                open = dateDataList.stream().mapToInt(WeShortLinkStat::getOpenNum).sum();
            }

            pvList.add(pv);
            uvList.add(uv);
            openList.add(open);
        }
        yData.put("pv", pvList);
        yData.put("uv", uvList);
        yData.put("open", openList);

        statisticsVo.setXAxis(dateList);
        statisticsVo.setYAxis(yData);

        return statisticsVo;
    }
}
