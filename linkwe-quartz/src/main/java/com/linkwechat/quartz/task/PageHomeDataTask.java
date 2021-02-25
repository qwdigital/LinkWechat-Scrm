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
        WePageCountDto newTime = weUserBehaviorDataService.getCountDataByDay(today,"day");
        /**
         * 昨日
         */
        //客户统计
        WePageCountDto lastTime = weUserBehaviorDataService.getCountDataByDay(yesterday,"day");

        WePageStaticDataDto.PageStaticData pageStaticData = setPageStaticData(newTime, lastTime);

        WePageStateQuery wePageStateQuery = new WePageStateQuery();
        //获取15天前的时间
        wePageStateQuery.setStartTime(DateUtil.offsetDay(new Date(), -15).toDateStr());
        wePageStateQuery.setEndTime(today);
        wePageStateQuery.setFew(14);
        List<WePageCountDto> dayCountData = weUserBehaviorDataService.getDayCountData(wePageStateQuery);
        pageStaticData.setDataList(dayCountData);

        return pageStaticData;
    }

    private WePageStaticDataDto.PageStaticData getWeekData(){
        /**
         * 本周
         */
        //客户统计
        WePageCountDto newTime = weUserBehaviorDataService.getCountDataByDay(DateUtil.today(),"week");
        /**
         * 上周
         */
        //客户统计
        WePageCountDto lastTime = weUserBehaviorDataService.getCountDataByDay(DateUtil.lastWeek().toDateStr(),"week");

        WePageStaticDataDto.PageStaticData pageStaticData = setPageStaticData(newTime, lastTime);

        WePageStateQuery wePageStateQuery = new WePageStateQuery();
        wePageStateQuery.setFew(5);
        List<WePageCountDto> weekCountData = weUserBehaviorDataService.getWeekCountData(wePageStateQuery);
        pageStaticData.setDataList(weekCountData);

        return pageStaticData;
    }

    private WePageStaticDataDto.PageStaticData getMonthData(){
        /**
         * 本周
         */
        //客户统计
        WePageCountDto newTime = weUserBehaviorDataService.getCountDataByDay(DateUtil.today(),"month");
        /**
         * 上周
         */
        //客户统计
        WePageCountDto lastTime = weUserBehaviorDataService.getCountDataByDay(DateUtil.lastMonth().toDateStr(),"month");

        WePageStaticDataDto.PageStaticData pageStaticData = setPageStaticData(newTime, lastTime);

        WePageStateQuery wePageStateQuery = new WePageStateQuery();
        wePageStateQuery.setFew(5);
        List<WePageCountDto> monthCountData = weUserBehaviorDataService.getMonthCountData(wePageStateQuery);
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

        return pageStaticData;
    }
}
