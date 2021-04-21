package com.linkwechat.quartz.task;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.dto.WePageCountDto;
import com.linkwechat.wecom.domain.dto.WePageStaticDataDto;
import com.linkwechat.wecom.domain.query.WePageStateQuery;
import com.linkwechat.wecom.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author danmo
 * @description 首页数据统计
 * @date 2021/2/23 23:51
 **/
@Slf4j
@Component("PageHomeDataTask")
public class PageHomeDataTask {
    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Autowired
    private IWeUserService weUserService;

    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeGroupService weGroupService;

    @Autowired
    private IWeGroupMemberService weGroupMemberService;

    @Autowired
    private IWeUserBehaviorDataService weUserBehaviorDataService;

    @Autowired
    private IWeGroupStatisticService weGroupStatisticService;

    @Autowired
    private RedisCache redisCache;


    public void getPageHomeDataData(){
        getCorpBasicData();
        getCorpRealTimeData();
    }

    public void getCorpBasicData(){
        //查询当前使用企业
        //WeCorpAccount weCorpAccount = weCorpAccountService.findValidWeCorpAccount();
        //String corpId = weCorpAccount.getCorpId();
        Map<String,Object> totalMap = new HashMap<>(16);
        //企业成员总数
        int userCount = weUserService.count(new LambdaQueryWrapper<WeUser>().eq(WeUser::getIsActivate, WeConstans.WE_USER_IS_ACTIVATE));
        //客户总人数
        int customerCount = weCustomerService.count();
        //客户群总数
        int groupCount = weGroupService.count();
        //群成员总数
        int groupMemberCount = weGroupMemberService.count();

        totalMap.put("userCount",userCount);
        totalMap.put("customerCount",customerCount);
        totalMap.put("groupCount",groupCount);
        totalMap.put("groupMemberCount",groupMemberCount);
        redisCache.setCacheMap("getCorpBasicData",totalMap);
    }


    public void getCorpRealTimeData(){
        WePageStaticDataDto wePageStaticDataDto = new WePageStaticDataDto();
        //今天
        wePageStaticDataDto.setToday(getTodayData());
        wePageStaticDataDto.setWeek(getWeekData());
        wePageStaticDataDto.setMonth(getMonthData());
        wePageStaticDataDto.setUpdateTime(DateUtil.now());
        redisCache.setCacheObject("getCorpRealTimeData",wePageStaticDataDto);
    }

    private WePageStaticDataDto.PageStaticData getTodayData(){
        /**
         * 今日
         */
        String today = DateUtil.today();
        String yesterday = DateUtil.yesterday().toDateStr();
        //客户统计
        WePageCountDto nowData = weUserBehaviorDataService.getCountDataByDay(today,"day");
        //客户群统计
        WePageCountDto nowTimeGroupChatData = weGroupStatisticService.getCountDataByDay(today, "day");
        if(nowTimeGroupChatData != null){
            nowData.setChatCnt(nowTimeGroupChatData.getChatCnt());
            nowData.setChatTotal(nowTimeGroupChatData.getChatTotal());
            nowData.setChatHasMsg(nowTimeGroupChatData.getChatHasMsg());
            nowData.setNewChatCnt(nowTimeGroupChatData.getNewChatCnt());
            nowData.setNewMemberCnt(nowTimeGroupChatData.getNewMemberCnt());
            nowData.setMemberTotal(nowTimeGroupChatData.getMemberTotal());
            nowData.setMsgTotal(nowTimeGroupChatData.getMsgTotal());
        }
        /**
         * 昨日
         */
        //客户统计
        WePageCountDto lastTime = weUserBehaviorDataService.getCountDataByDay(yesterday,"day");
        //客户群统计
        WePageCountDto lastGroupChatTime = weGroupStatisticService.getCountDataByDay(yesterday,"day");
        if(lastGroupChatTime != null){
            lastTime.setChatCnt(lastGroupChatTime.getChatCnt());
            lastTime.setChatTotal(lastGroupChatTime.getChatTotal());
            lastTime.setChatHasMsg(lastGroupChatTime.getChatHasMsg());
            lastTime.setNewChatCnt(lastGroupChatTime.getNewChatCnt());
            lastTime.setNewMemberCnt(lastGroupChatTime.getNewMemberCnt());
            lastTime.setMemberTotal(lastGroupChatTime.getMemberTotal());
            lastTime.setMsgTotal(lastGroupChatTime.getMsgTotal());
        }

        WePageStaticDataDto.PageStaticData pageStaticData = setPageStaticData(nowData, lastTime);

        WePageStateQuery wePageStateQuery = new WePageStateQuery();
        //获取15天前的时间
        wePageStateQuery.setStartTime(DateUtil.offsetDay(new Date(), -15).toDateStr());
        wePageStateQuery.setEndTime(today);
        wePageStateQuery.setFew(14);
        List<WePageCountDto> dayCountData = weUserBehaviorDataService.getDayCountData(wePageStateQuery);
        List<WePageCountDto> dayGroupChatCountData = weGroupStatisticService.getDayCountData(wePageStateQuery);
        for (WePageCountDto dayData : dayCountData) {
            for (WePageCountDto dayGroupChatData : dayGroupChatCountData) {
                if (dayData.getXTime().equals(dayGroupChatData.getXTime())){
                    dayData.setChatCnt(dayGroupChatData.getChatCnt());
                    dayData.setChatTotal(dayGroupChatData.getChatTotal());
                    dayData.setChatHasMsg(dayGroupChatData.getChatHasMsg());
                    dayData.setNewChatCnt(dayGroupChatData.getNewChatCnt());
                    dayData.setNewMemberCnt(dayGroupChatData.getNewMemberCnt());
                    dayData.setMemberTotal(dayGroupChatData.getMemberTotal());
                    dayData.setMsgTotal(dayGroupChatData.getMsgTotal());
                    break;
                }
            }
        }
        pageStaticData.setDataList(dayCountData);

        return pageStaticData;
    }

    private WePageStaticDataDto.PageStaticData getWeekData(){
        /**
         * 本周
         */
        //客户统计
        WePageCountDto newTime = weUserBehaviorDataService.getCountDataByDay(DateUtil.today(),"week");
        WePageCountDto nowTimeGroupChatData = weGroupStatisticService.getCountDataByDay(DateUtil.today(),"week");
        if(nowTimeGroupChatData != null){
            newTime.setChatCnt(nowTimeGroupChatData.getChatCnt());
            newTime.setChatTotal(nowTimeGroupChatData.getChatTotal());
            newTime.setChatHasMsg(nowTimeGroupChatData.getChatHasMsg());
            newTime.setNewChatCnt(nowTimeGroupChatData.getNewChatCnt());
            newTime.setNewMemberCnt(nowTimeGroupChatData.getNewMemberCnt());
            newTime.setMemberTotal(nowTimeGroupChatData.getMemberTotal());
            newTime.setMsgTotal(nowTimeGroupChatData.getMsgTotal());
        }
        /**
         * 上周
         */
        //客户统计
        WePageCountDto lastTime = weUserBehaviorDataService.getCountDataByDay(DateUtil.lastWeek().toDateStr(),"week");
        WePageCountDto lastTimeGroupChatData = weGroupStatisticService.getCountDataByDay(DateUtil.lastWeek().toDateStr(),"week");
        if(lastTimeGroupChatData != null){
            lastTime.setChatCnt(lastTimeGroupChatData.getChatCnt());
            lastTime.setChatTotal(lastTimeGroupChatData.getChatTotal());
            lastTime.setChatHasMsg(lastTimeGroupChatData.getChatHasMsg());
            lastTime.setNewChatCnt(lastTimeGroupChatData.getNewChatCnt());
            lastTime.setNewMemberCnt(lastTimeGroupChatData.getNewMemberCnt());
            lastTime.setMemberTotal(lastTimeGroupChatData.getMemberTotal());
            lastTime.setMsgTotal(lastTimeGroupChatData.getMsgTotal());
        }
        WePageStaticDataDto.PageStaticData pageStaticData = setPageStaticData(newTime, lastTime);

        WePageStateQuery wePageStateQuery = new WePageStateQuery();
        wePageStateQuery.setFew(5);
        List<WePageCountDto> weekCountData = weUserBehaviorDataService.getWeekCountData(wePageStateQuery);
        List<WePageCountDto> weekGroupChatCountData = weGroupStatisticService.getWeekCountData(wePageStateQuery);
        for (WePageCountDto weekData : weekCountData) {
            for (WePageCountDto weekGroupChatData : weekGroupChatCountData) {
                if (weekData.getXTime().equals(weekGroupChatData.getXTime())){
                    weekData.setChatCnt(weekGroupChatData.getChatCnt());
                    weekData.setChatTotal(weekGroupChatData.getChatTotal());
                    weekData.setChatHasMsg(weekGroupChatData.getChatHasMsg());
                    weekData.setNewChatCnt(weekGroupChatData.getNewChatCnt());
                    weekData.setNewMemberCnt(weekGroupChatData.getNewMemberCnt());
                    weekData.setMemberTotal(weekGroupChatData.getMemberTotal());
                    weekData.setMsgTotal(weekGroupChatData.getMsgTotal());
                    break;
                }
            }
        }
        pageStaticData.setDataList(weekCountData);

        return pageStaticData;
    }

    private WePageStaticDataDto.PageStaticData getMonthData(){
        /**
         * 本月
         */
        //客户统计
        WePageCountDto newTime = weUserBehaviorDataService.getCountDataByDay(DateUtil.today(),"month");
        WePageCountDto nowTimeGroupChatData = weGroupStatisticService.getCountDataByDay(DateUtil.today(),"month");
        if(nowTimeGroupChatData != null){
            newTime.setChatCnt(nowTimeGroupChatData.getChatCnt());
            newTime.setChatTotal(nowTimeGroupChatData.getChatTotal());
            newTime.setChatHasMsg(nowTimeGroupChatData.getChatHasMsg());
            newTime.setNewChatCnt(nowTimeGroupChatData.getNewChatCnt());
            newTime.setNewMemberCnt(nowTimeGroupChatData.getNewMemberCnt());
            newTime.setMemberTotal(nowTimeGroupChatData.getMemberTotal());
            newTime.setMsgTotal(nowTimeGroupChatData.getMsgTotal());
        }
        /**
         * 上月
         */
        //客户统计
        WePageCountDto lastTime = weUserBehaviorDataService.getCountDataByDay(DateUtil.lastMonth().toDateStr(),"month");
        WePageCountDto lastTimeGroupChatData = weGroupStatisticService.getCountDataByDay(DateUtil.lastMonth().toDateStr(),"month");
        if(lastTimeGroupChatData != null){
            lastTime.setChatCnt(lastTimeGroupChatData.getChatCnt());
            lastTime.setChatTotal(lastTimeGroupChatData.getChatTotal());
            lastTime.setChatHasMsg(lastTimeGroupChatData.getChatHasMsg());
            lastTime.setNewChatCnt(lastTimeGroupChatData.getNewChatCnt());
            lastTime.setNewMemberCnt(lastTimeGroupChatData.getNewMemberCnt());
            lastTime.setMemberTotal(lastTimeGroupChatData.getMemberTotal());
            lastTime.setMsgTotal(lastTimeGroupChatData.getMsgTotal());
        }
        WePageStaticDataDto.PageStaticData pageStaticData = setPageStaticData(newTime, lastTime);

        WePageStateQuery wePageStateQuery = new WePageStateQuery();
        wePageStateQuery.setFew(5);
        List<WePageCountDto> monthCountData = weUserBehaviorDataService.getMonthCountData(wePageStateQuery);
        List<WePageCountDto> monthGroupChatCountData = weGroupStatisticService.getMonthCountData(wePageStateQuery);
        for (WePageCountDto monthData : monthCountData) {
            for (WePageCountDto monthGroupChatData : monthGroupChatCountData) {
                if (monthData.getXTime().equals(monthGroupChatData.getXTime())){
                    monthData.setChatCnt(monthGroupChatData.getChatCnt());
                    monthData.setChatTotal(monthGroupChatData.getChatTotal());
                    monthData.setChatHasMsg(monthGroupChatData.getChatHasMsg());
                    monthData.setNewChatCnt(monthGroupChatData.getNewChatCnt());
                    monthData.setNewMemberCnt(monthGroupChatData.getNewMemberCnt());
                    monthData.setMemberTotal(monthGroupChatData.getMemberTotal());
                    monthData.setMsgTotal(monthGroupChatData.getMsgTotal());
                    break;
                }
            }
        }
        pageStaticData.setDataList(monthCountData);

        return pageStaticData;
    }


    private WePageStaticDataDto.PageStaticData setPageStaticData(WePageCountDto nowTime,WePageCountDto lastTime){
        WePageStaticDataDto.PageStaticData pageStaticData = new WePageStaticDataDto.PageStaticData();

        pageStaticData.setNewApplyCnt(nowTime.getNewApplyCnt());
        pageStaticData.setNewApplyCntDiff(nowTime.getNewApplyCnt() - lastTime.getNewApplyCnt());
        pageStaticData.setNegativeFeedbackCnt(nowTime.getNegativeFeedbackCnt());
        pageStaticData.setNegativeFeedbackCntDiff(nowTime.getNegativeFeedbackCnt() - lastTime.getNegativeFeedbackCnt());
        pageStaticData.setNewContactCnt(nowTime.getNewContactCnt());
        pageStaticData.setNewContactCntDiff(nowTime.getNewContactCnt() - lastTime.getNewContactCnt());
        pageStaticData.setNewMemberCnt(nowTime.getNewMemberCnt());
        pageStaticData.setNewMemberCntDiff(nowTime.getNewMemberCnt() - lastTime.getNewMemberCnt());

        return pageStaticData;
    }
}
