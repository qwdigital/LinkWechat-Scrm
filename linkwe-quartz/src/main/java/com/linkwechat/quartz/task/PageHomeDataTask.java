package com.linkwechat.quartz.task;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.WePageCountDto;
import com.linkwechat.wecom.domain.dto.WePageStaticDataDto;
import com.linkwechat.wecom.domain.query.WePageStateQuery;
import com.linkwechat.wecom.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 首页数据统计
 * @date 2021/2/23 23:51
 **/
@Slf4j
@Component("PageHomeDataTask")
public class PageHomeDataTask {

    @Autowired
    private IWeUserService weUserService;

    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeGroupService weGroupService;

    @Autowired
    private IWeGroupMemberService weGroupMemberService;

    @Autowired
    private IWePageStatisticsService wePageStatisticsService;

    @Autowired
    private RedisCache redisCache;


    /**
     * 统计数据
     */
    public void getPageHomeDataData() {
        //新增客户数
        int newCustomerCnt = weCustomerService.count(new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getDelFlag, 0).apply("date_format(first_add_time,'%Y-%m-%d') = {0}", DateUtil.today()));
        //流失客户数
        int negativeFeedbackCnt = weCustomerService.count(new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getDelFlag, 1).apply("date_format(first_add_time,'%Y-%m-%d') = {0}", DateUtil.today()));
        //新增客户群数
        int newGroupCnt = weGroupService.count(new LambdaQueryWrapper<WeGroup>().eq(WeGroup::getDelFlag, 0).apply("date_format(add_time,'%Y-%m-%d') = {0}", DateUtil.today()));
        //群新增成员数
        int newMemberCnt = weGroupMemberService.count(new LambdaQueryWrapper<WeGroupMember>().eq(WeGroupMember::getDelFlag, 0).apply("date_format(join_time,'%Y-%m-%d') = {0}", DateUtil.today()));
        WePageStatistics pageStatistics = new WePageStatistics();
        pageStatistics.setNewContactCnt(newCustomerCnt);
        pageStatistics.setNegativeFeedbackCnt(negativeFeedbackCnt);
        pageStatistics.setNewChatCnt(newGroupCnt);
        pageStatistics.setNewMemberCnt(newMemberCnt);
        pageStatistics.setRefreshTime(new Date());
        boolean result = wePageStatisticsService.saveOrUpdate(pageStatistics, new LambdaQueryWrapper<WePageStatistics>().apply("date_format(refresh_time,'%Y-%m-%d') = {0}", DateUtil.today()));
        if (result) {
            getCorpBasicData();
            getCorpRealTimeData();
        }
    }

    private void getCorpBasicData() {
        Map<String, Integer> totalMap = new HashMap<>(16);
        //企业成员总数
        int userCount = weUserService.count(new LambdaQueryWrapper<WeUser>().eq(WeUser::getIsActivate, WeConstans.WE_USER_IS_ACTIVATE));
        //客户总人数
        int customerCount = weCustomerService.count();
        //客户群总数
        int groupCount = weGroupService.count();
        //群成员总数
        int groupMemberCount = weGroupMemberService.count();

        totalMap.put("userCount", userCount);
        totalMap.put("customerCount", customerCount);
        totalMap.put("groupCount", groupCount);
        totalMap.put("groupMemberCount", groupMemberCount);
        redisCache.setCacheMap("getCorpBasicData", totalMap);
    }


    private void getCorpRealTimeData() {
        WePageStaticDataDto wePageStaticDataDto = new WePageStaticDataDto();
        //今天
        wePageStaticDataDto.setToday(getTodayData());
        wePageStaticDataDto.setWeek(getWeekData());
        wePageStaticDataDto.setMonth(getMonthData());
        wePageStaticDataDto.setUpdateTime(DateUtil.now());
        redisCache.setCacheObject("getCorpRealTimeData", wePageStaticDataDto);
    }

    private WePageStaticDataDto.PageStaticData getTodayData() {
        /**
         * 今日
         */
        String today = DateUtil.today();
        String yesterday = DateUtil.yesterday().toDateStr();
        //获取15天前的时间
        WePageStateQuery wePageStateQuery = new WePageStateQuery();
        wePageStateQuery.setStartTime(DateUtil.offsetDay(new Date(), -15).toDateStr());
        wePageStateQuery.setEndTime(today);
        wePageStateQuery.setFew(14);
        List<WePageCountDto> pageCountList = wePageStatisticsService.getDayCountData(wePageStateQuery);
        Map<String, List<WePageCountDto>> pageCountMap = pageCountList.stream().collect(Collectors.groupingBy(WePageCountDto::getXTime));
        WePageCountDto nowPageCount = Optional.ofNullable(pageCountMap.get(today).get(0)).orElseGet(WePageCountDto::new);
        WePageCountDto lastPageCount = Optional.ofNullable(pageCountMap.get(yesterday).get(0)).orElseGet(WePageCountDto::new);
        WePageStaticDataDto.PageStaticData pageStaticData = setPageStaticData(nowPageCount, lastPageCount);
        pageStaticData.setDataList(pageCountList);
        return pageStaticData;
    }

    private WePageStaticDataDto.PageStaticData getWeekData() {
        /**
         * 本周
         */
        //int nowWeek = DateUtil.weekOfYear(new Date());
        //int lastWeek = DateUtil.weekOfYear(DateUtil.lastWeek());

        WePageStateQuery wePageStateQuery = new WePageStateQuery();
        wePageStateQuery.setFew(5);
        List<WePageCountDto> pageCountList = wePageStatisticsService.getWeekCountData(wePageStateQuery);
        WePageCountDto nowPageCount = pageCountList.get(pageCountList.size()-1);
        WePageCountDto lastPageCount = pageCountList.get(pageCountList.size()-2);
        WePageStaticDataDto.PageStaticData pageStaticData = setPageStaticData(nowPageCount, lastPageCount);
        pageStaticData.setDataList(pageCountList);
        return pageStaticData;
    }

    private WePageStaticDataDto.PageStaticData getMonthData() {
        /**
         * 本月
         */
        //String nowMonth = DateUtil.format(new Date(), "yyyy-MM");
        //String lastMonth = DateUtil.format(DateUtil.lastMonth(), "yyyy-MM");
        WePageStateQuery wePageStateQuery = new WePageStateQuery();
        wePageStateQuery.setFew(5);
        List<WePageCountDto> pageCountList = wePageStatisticsService.getMonthCountData(wePageStateQuery);
        WePageCountDto nowPageCount = pageCountList.get(pageCountList.size()-1);
        WePageCountDto lastPageCount = pageCountList.get(pageCountList.size()-2);
        WePageStaticDataDto.PageStaticData pageStaticData = setPageStaticData(nowPageCount, lastPageCount);
        pageStaticData.setDataList(pageCountList);
        return pageStaticData;
    }


    private WePageStaticDataDto.PageStaticData setPageStaticData(WePageCountDto nowTime, WePageCountDto lastTime) {
        WePageStaticDataDto.PageStaticData pageStaticData = new WePageStaticDataDto.PageStaticData();

        //pageStaticData.setNewApplyCnt(nowTime.getNewApplyCnt());
        //pageStaticData.setNewApplyCntDiff(nowTime.getNewApplyCnt() - lastTime.getNewApplyCnt());
        pageStaticData.setNegativeFeedbackCnt(nowTime.getNegativeFeedbackCnt());
        pageStaticData.setNegativeFeedbackCntDiff(nowTime.getNegativeFeedbackCnt() - lastTime.getNegativeFeedbackCnt());
        pageStaticData.setNewContactCnt(nowTime.getNewContactCnt());
        pageStaticData.setNewContactCntDiff(nowTime.getNewContactCnt() - lastTime.getNewContactCnt());
        pageStaticData.setNewMemberCnt(nowTime.getNewMemberCnt());
        pageStaticData.setNewMemberCntDiff(nowTime.getNewMemberCnt() - lastTime.getNewMemberCnt());
        pageStaticData.setNewChatCnt(nowTime.getNewChatCnt());
        pageStaticData.setNewChatCntDiff(nowTime.getNewChatCnt() - lastTime.getNewChatCnt());

        return pageStaticData;
    }
}
