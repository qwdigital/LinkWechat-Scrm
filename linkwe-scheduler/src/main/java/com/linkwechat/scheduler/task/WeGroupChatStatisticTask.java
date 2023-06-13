package com.linkwechat.scheduler.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.WeGroupStatistic;
import com.linkwechat.domain.wecom.query.customer.state.WeGroupChatStatisticQuery;
import com.linkwechat.domain.wecom.vo.customer.state.WeGroupChatStatisticVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.service.IWeGroupService;
import com.linkwechat.service.IWeGroupStatisticService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 群聊数据统计
 * @date 2022/5/24 0:42
 **/
@Slf4j
@Component
public class WeGroupChatStatisticTask {
    @Autowired
    private QwCustomerClient qwCustomerClient;
    @Autowired
    private IWeGroupService weGroupService;
    @Autowired
    private IWeGroupStatisticService weGroupStatisticService;

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @XxlJob("weGroupChatStatisticTask")
    public void process(){
        String params = XxlJobHelper.getJobParam();
        log.info("群聊数据统计>>>>>>>>>>>>>>>>>>>启动 params:{}",params);
        List<WeCorpAccount> accountList = weCorpAccountService.getAllCorpAccountInfo();

        if (CollectionUtil.isNotEmpty(accountList)) {
            for (WeCorpAccount corpAccountInfo : accountList) {
                ThreadUtil.execute(() -> getGroupChatData(corpAccountInfo, params));
            }
        }
    }


    public void getGroupChatData(WeCorpAccount weCorpAccount, String params) {
        log.info("群聊数据 当前统计租户为 corpId:{}",weCorpAccount.getCorpId());
        WeUserBehaviorDataTask.UserBehaviorDataTime userBehaviorDataTime = new WeUserBehaviorDataTask.UserBehaviorDataTime();
        if (StringUtils.isNotEmpty(params)) {
            userBehaviorDataTime = JSONObject.parseObject(params, WeUserBehaviorDataTask.UserBehaviorDataTime.class);
        }
        List<WeGroup> weGroupList = weGroupService.list(new LambdaQueryWrapper<WeGroup>().eq(WeGroup::getDelFlag, 0));
        if (CollectionUtil.isNotEmpty(weGroupList)){
            List<String> ownerList = weGroupList.stream().map(WeGroup::getOwner).distinct().collect(Collectors.toList());
            List<List<String>> partitionList = Lists.partition(ownerList, 100);
            List<WeGroupStatistic> weGroupStatisticList = new ArrayList<>();
            for (List<String> owners : partitionList) {
                WeGroupChatStatisticQuery query = new WeGroupChatStatisticQuery();
                //前一天的数据
                Long startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(),userBehaviorDataTime.getBeginTime())).getTime()/1000;
                Long endTime = DateUtil.endOfDay(DateUtil.offsetDay(new Date(),userBehaviorDataTime.getEndTime())).getTime()/1000;
                query.setDay_begin_time(startTime);
                query.setDay_end_time(endTime);
                WeGroupChatStatisticQuery.OwnerFilter ownerFilter = new WeGroupChatStatisticQuery.OwnerFilter();
                ownerFilter.setUserid_list(owners);
                query.setOwnerFilter(ownerFilter);
                try {
                    WeGroupChatStatisticVo groupChatStatistic = qwCustomerClient.getGroupChatStatisticByDay(query).getData();
                    if(groupChatStatistic != null && CollectionUtil.isNotEmpty(groupChatStatistic.getItems())){
                        List<WeGroupChatStatisticVo.GroupchatStatisticData> items = groupChatStatistic.getItems();
                        items.forEach(groupchatStatisticData -> {
                            WeGroupStatistic weGroupStatistic = new WeGroupStatistic();
                            WeGroupChatStatisticVo.StatisticData data = groupchatStatisticData.getData();
                            BeanUtils.copyPropertiesignoreOther(data, weGroupStatistic);
                            weGroupStatistic.setStatTime(groupchatStatisticData.getStatTime());
                            weGroupStatisticList.add(weGroupStatistic);
                        });
                    }

                } catch (Exception e) {
                    log.error("群聊数据拉取失败: query:{}", JSONObject.toJSONString(query),e);
                }
            }
            if(CollectionUtil.isNotEmpty(weGroupStatisticList)){
                Map<Date, List<WeGroupStatistic>> dateListMap = weGroupStatisticList.stream().collect(Collectors.groupingBy(WeGroupStatistic::getStatTime));
                List<WeGroupStatistic> resList = new ArrayList<>();
                dateListMap.forEach((sendTime, statisticList) ->{
                    WeGroupStatistic statistic = new WeGroupStatistic();
                    statistic.setChatTotal(statisticList.stream().mapToInt(WeGroupStatistic::getChatTotal).sum());
                    statistic.setChatHasMsg(statisticList.stream().mapToInt(WeGroupStatistic::getChatHasMsg).sum());
                    statistic.setNewChatCnt(statisticList.stream().mapToInt(WeGroupStatistic::getNewChatCnt).sum());
                    statistic.setNewMemberCnt(statisticList.stream().mapToInt(WeGroupStatistic::getNewMemberCnt).sum());
                    statistic.setMemberTotal(statisticList.stream().mapToInt(WeGroupStatistic::getMemberTotal).sum());
                    statistic.setNewChatCnt(statisticList.stream().mapToInt(WeGroupStatistic::getNewMemberCnt).sum());
                    statistic.setMemberHasMsg(statisticList.stream().mapToInt(WeGroupStatistic::getMemberHasMsg).sum());
                    statistic.setMsgTotal(statisticList.stream().mapToInt(WeGroupStatistic::getMsgTotal).sum());
                    statistic.setStatTime(sendTime);
                    resList.add(statistic);
                });
                weGroupStatisticService.saveBatch(resList);
            }

        }
    }
}
