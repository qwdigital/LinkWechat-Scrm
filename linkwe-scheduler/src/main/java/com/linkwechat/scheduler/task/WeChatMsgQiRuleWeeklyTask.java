package com.linkwechat.scheduler.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.thread.WeMsgQiRuleThreadExecutor;
import com.linkwechat.domain.*;
import com.linkwechat.domain.qirule.query.WeQiRuleListQuery;
import com.linkwechat.domain.qirule.vo.WeQiRuleListVo;
import com.linkwechat.domain.qirule.vo.WeQiRuleUserVo;
import com.linkwechat.service.*;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 质检周报计算任务
 *
 * @author danmo
 * @date 2023年05月18日 13:42
 */
@Slf4j
@Component
public class WeChatMsgQiRuleWeeklyTask {

    @Autowired
    private IWeQiRuleService weQiRuleService;

    @Autowired
    private IWeQiRuleScopeService weQiRuleScopeService;

    @Autowired
    private IWeChatContactMsgService weChatContactMsgService;

    @Autowired
    private IWeGroupService weGroupService;

    @Autowired
    private IWeQiRuleMsgService weQiRuleMsgService;

    @Autowired
    private IWeQiRuleUserStatisticsService weQiRuleUserStatisticsService;

    @Autowired
    private IWeQiRuleManageStatisticsService weQiRuleManageStatisticsService;

    @Autowired
    private IWeQiRuleWeeklyUserDataService weQiRuleWeeklyUserDataService;

    /**
     * 员工统计执行器
     */
    @XxlJob("weChatMsgQiRuleUserStatisticsTask")
    public void weChatMsgQiRuleUserStatisticsHandler() {
        WeChatMsgQiRuleWeeklyTaskParams weeklyTaskParams = new WeChatMsgQiRuleWeeklyTaskParams();
        String jobParam = XxlJobHelper.getJobParam();
        if (StringUtils.isNotEmpty(jobParam)) {
            weeklyTaskParams = JSONObject.parseObject(jobParam, WeChatMsgQiRuleWeeklyTaskParams.class);
        }

        LambdaQueryWrapper<WeQiRuleScope> wrapper = new LambdaQueryWrapper<WeQiRuleScope>()
                .select(WeQiRuleScope::getUserId)
                .eq(WeQiRuleScope::getDelFlag, 0)
                .groupBy(WeQiRuleScope::getUserId);
        if(StringUtils.isNotEmpty(weeklyTaskParams.getUserIds())){
            wrapper.in(WeQiRuleScope::getUserId, Arrays.stream(weeklyTaskParams.getUserIds().split(",")).collect(Collectors.toList()));
        }

        List<WeQiRuleScope> weQiRuleScopes = weQiRuleScopeService.list(wrapper);

        DateTime startTime = StringUtils.isNotEmpty(weeklyTaskParams.getStartTime()) ? DateUtil.parseDate(weeklyTaskParams.getStartTime()) : DateUtil.yesterday();
        DateTime endTime = StringUtils.isNotEmpty(weeklyTaskParams.getEndTime()) ? DateUtil.parseDate(weeklyTaskParams.getEndTime()) : DateUtil.yesterday();

        List<DateTime> dateTimes = DateUtil.rangeToList(startTime, endTime, DateField.DAY_OF_YEAR);
        for (DateTime dateTime : dateTimes) {
            if (CollectionUtil.isNotEmpty(weQiRuleScopes)) {
                for (WeQiRuleScope weQiRuleScope : weQiRuleScopes) {
                    WeMsgQiRuleThreadExecutor.getInstance().execute(() -> calculateUserDataHandler(weQiRuleScope, dateTime));
                }
            }
        }
    }

    /**
     * 周报统计执行器
     */
    @XxlJob("weChatMsgQiRuleWeeklyStatisticsTask")
    public void weChatMsgQiRuleWeeklyStatisticsHandler() {
        String jobParam = XxlJobHelper.getJobParam();

        DateTime startTime = DateUtil.offsetWeek(DateUtil.yesterday(), -1);
        DateTime endTime = DateUtil.yesterday();

        WeQiRuleListQuery query = new WeQiRuleListQuery();
        query.setIsShow(false);
        List<WeQiRuleListVo> ruleList = weQiRuleService.getQiRuleList(query);

        if (CollectionUtil.isNotEmpty(ruleList)) {
            Map<String, List<String>> manageUserMap = new HashMap<>(64);

            for (WeQiRuleListVo weQiRule : ruleList) {
                for (String userId : weQiRule.getManageUser().split(",")) {
                    List<String> ruleIdList = Optional.ofNullable(manageUserMap.get("userId")).orElseGet(ArrayList::new);
                    List<String> scopeUserIds = Optional.ofNullable(weQiRule.getQiRuleScope()).orElseGet(ArrayList::new).stream().map(WeQiRuleUserVo::getUserId).collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(scopeUserIds)) {
                        ruleIdList.addAll(scopeUserIds);
                        manageUserMap.put(userId, ruleIdList);
                    }
                }
            }

            if (CollectionUtil.isNotEmpty(manageUserMap)) {

                Set<String> scopeUserIdList = manageUserMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());

                List<WeQiRuleUserStatistics> userStatisticsList = weQiRuleUserStatisticsService.list(new LambdaQueryWrapper<WeQiRuleUserStatistics>()
                        .in(WeQiRuleUserStatistics::getWeUserId, scopeUserIdList)
                        .apply("date_format(state_time, '%Y-%m-%d' ) >= '" + startTime.toDateStr() + "'")
                        .apply("date_format(state_time, '%Y-%m-%d' ) <= '" + endTime.toDateStr() + "'")
                );
                manageUserMap.forEach((userId, scopeUserIds) -> {

                    WeQiRuleManageStatistics manageStatistics = new WeQiRuleManageStatistics();
                    manageStatistics.setWeUserId(userId);
                    manageStatistics.setStaffNum(new HashSet<>(scopeUserIds).size());
                    manageStatistics.setStartTime(startTime);
                    manageStatistics.setFinishTime(endTime);

                    List<WeQiRuleUserStatistics> ruleUserStatistics = userStatisticsList.stream().filter(userStatistics -> scopeUserIds.stream().anyMatch(scopeUserId -> ObjectUtil.equal(scopeUserId, userStatistics.getWeUserId()))).collect(Collectors.toList());

                    if (CollectionUtil.isNotEmpty(ruleUserStatistics)) {

                        //客户会话数
                        BigDecimal chatNum = ruleUserStatistics.stream()
                                .filter(item -> StringUtils.isNotEmpty(item.getChatNum()))
                                .map(item -> new BigDecimal(item.getChatNum()))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        manageStatistics.setChatNum(chatNum.toString());

                        //客群会话数
                        BigDecimal groupChatNum = ruleUserStatistics.stream()
                                .filter(item -> StringUtils.isNotEmpty(item.getGroupChatNum()))
                                .map(item -> new BigDecimal(item.getGroupChatNum()))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        manageStatistics.setGroupChatNum(groupChatNum.toString());

                        //成员回复次数
                        BigDecimal replyNum = ruleUserStatistics.stream()
                                .filter(item -> StringUtils.isNotEmpty(item.getReplyNum()))
                                .map(item -> new BigDecimal(item.getReplyNum()))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        manageStatistics.setReplyNum(replyNum.toString());

                        //成员超时次数
                        BigDecimal timeOutNum = ruleUserStatistics.stream()
                                .filter(item -> StringUtils.isNotEmpty(item.getTimeOutNum()))
                                .map(item -> new BigDecimal(item.getTimeOutNum()))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        manageStatistics.setTimeOutNum(timeOutNum.toString());

                        //成员超时率
                        double timeOutRate = ruleUserStatistics.stream()
                                .filter(item -> StringUtils.isNotEmpty(item.getTimeOutRate()))
                                .mapToDouble(item -> Double.parseDouble(item.getTimeOutRate()))
                                .summaryStatistics().getAverage();
                        manageStatistics.setTimeOutRate(new BigDecimal(timeOutRate).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

                        //客户会话超时率
                        double chatTimeOutRate = ruleUserStatistics.stream()
                                .filter(item -> StringUtils.isNotEmpty(item.getChatTimeOutRate()))
                                .mapToDouble(item -> Double.parseDouble(item.getChatTimeOutRate()))
                                .summaryStatistics().getAverage();
                        manageStatistics.setChatTimeOutRate(new BigDecimal(chatTimeOutRate).setScale(2, BigDecimal.ROUND_HALF_UP).toString());


                        //客群会话超时率
                        double groupChatTimeOutRate = ruleUserStatistics.stream()
                                .filter(item -> StringUtils.isNotEmpty(item.getGroupChatTimeOutRate()))
                                .mapToDouble(item -> Double.parseDouble(item.getGroupChatTimeOutRate()))
                                .summaryStatistics().getAverage();
                        manageStatistics.setGroupChatTimeOutRate(new BigDecimal(groupChatTimeOutRate).setScale(2, BigDecimal.ROUND_HALF_UP).toString());


                    }

                    weQiRuleManageStatisticsService.save(manageStatistics);

                    Map<String, List<WeQiRuleUserStatistics>> ruleUserStatisticsMap = ruleUserStatistics.stream().collect(Collectors.groupingBy(WeQiRuleUserStatistics::getWeUserId));

                    ruleUserStatisticsMap.forEach((scopeUserId, userStatistics) -> {
                        WeQiRuleWeeklyUserData weQiRuleWeeklyUserData = new WeQiRuleWeeklyUserData();
                        weQiRuleWeeklyUserData.setWeeklyId(manageStatistics.getId());
                        weQiRuleWeeklyUserData.setUserId(scopeUserId);
                        //客户会话数
                        BigDecimal chatNum = userStatistics.stream()
                                .filter(item -> StringUtils.isNotEmpty(item.getChatNum()))
                                .map(item -> new BigDecimal(item.getChatNum()))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        weQiRuleWeeklyUserData.setChatNum(chatNum.toString());

                        //客群会话数
                        BigDecimal groupChatNum = userStatistics.stream()
                                .filter(item -> StringUtils.isNotEmpty(item.getGroupChatNum()))
                                .map(item -> new BigDecimal(item.getGroupChatNum()))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        weQiRuleWeeklyUserData.setGroupChatNum(groupChatNum.toString());

                        //成员回复次数
                        BigDecimal replyNum = userStatistics.stream()
                                .filter(item -> StringUtils.isNotEmpty(item.getReplyNum()))
                                .map(item -> new BigDecimal(item.getReplyNum()))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        weQiRuleWeeklyUserData.setReplyNum(replyNum.toString());

                        //成员超时次数
                        BigDecimal timeOutNum = userStatistics.stream()
                                .filter(item -> StringUtils.isNotEmpty(item.getTimeOutNum()))
                                .map(item -> new BigDecimal(item.getTimeOutNum()))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        weQiRuleWeeklyUserData.setTimeOutNum(timeOutNum.toString());

                        //成员超时率
                        double timeOutRate = userStatistics.stream()
                                .filter(item -> StringUtils.isNotEmpty(item.getTimeOutRate()))
                                .mapToDouble(item -> Double.parseDouble(item.getTimeOutRate()))
                                .summaryStatistics().getAverage();
                        weQiRuleWeeklyUserData.setTimeOutRate(new BigDecimal(timeOutRate).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

                        //客户会话超时率
                        double chatTimeOutRate = userStatistics.stream()
                                .filter(item -> StringUtils.isNotEmpty(item.getChatTimeOutRate()))
                                .mapToDouble(item -> Double.parseDouble(item.getChatTimeOutRate()))
                                .summaryStatistics().getAverage();
                        weQiRuleWeeklyUserData.setChatTimeOutRate(new BigDecimal(chatTimeOutRate).setScale(2, BigDecimal.ROUND_HALF_UP).toString());


                        //客群会话超时率
                        double groupChatTimeOutRate = userStatistics.stream()
                                .filter(item -> StringUtils.isNotEmpty(item.getGroupChatTimeOutRate()))
                                .mapToDouble(item -> Double.parseDouble(item.getGroupChatTimeOutRate()))
                                .summaryStatistics().getAverage();
                        weQiRuleWeeklyUserData.setGroupChatTimeOutRate(new BigDecimal(groupChatTimeOutRate).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

                        weQiRuleWeeklyUserDataService.save(weQiRuleWeeklyUserData);
                    });

                });
            }
        }

    }


    /**
     * 计算员工数据
     *
     * @param weQiRuleScope 员工信息
     * @param dateTime      统计时间
     */
    private void calculateUserDataHandler(WeQiRuleScope weQiRuleScope, DateTime dateTime) {

        String userId = weQiRuleScope.getUserId();

        WeQiRuleUserStatistics statistics = new WeQiRuleUserStatistics();
        statistics.setWeUserId(userId);
        statistics.setStateTime(dateTime);
        //客户会话数
        int chatNum = weChatContactMsgService.count(new LambdaQueryWrapper<WeChatContactMsg>()
                .and(wrapper -> wrapper.eq(WeChatContactMsg::getFromId, userId)
                        .or().eq(WeChatContactMsg::getToList, userId))
                .isNull(WeChatContactMsg::getRoomId)
                .apply("date_format(msg_time, '%Y-%m-%d' ) = '" + dateTime.toDateStr() + "'")
                .eq(WeChatContactMsg::getDelFlg, 0));
        statistics.setChatNum(String.valueOf(chatNum));

        //客群会话数
        List<WeGroup> groupList = Optional.ofNullable(weGroupService.list(new LambdaQueryWrapper<WeGroup>().select(WeGroup::getChatId).eq(WeGroup::getOwner, userId).eq(WeGroup::getDelFlag, 0))).orElseGet(ArrayList::new);

        if(CollectionUtil.isNotEmpty(groupList)){
            int groupChatNum = weChatContactMsgService.count(new LambdaQueryWrapper<WeChatContactMsg>()
                    .in(WeChatContactMsg::getRoomId, groupList.stream().map(WeGroup::getChatId).collect(Collectors.toList()))
                    .apply("date_format(msg_time, '%Y-%m-%d' ) = '" + dateTime.toDateStr() + "'")
                    .eq(WeChatContactMsg::getDelFlg, 0));
            statistics.setGroupChatNum(String.valueOf(groupChatNum));
        }else {
            statistics.setGroupChatNum("0");
        }


        List<WeQiRuleMsg> replyNumList = Optional.ofNullable(weQiRuleMsgService.list(new LambdaQueryWrapper<WeQiRuleMsg>()
                .eq(WeQiRuleMsg::getReceiveId, userId)
                .eq(WeQiRuleMsg::getReplyStatus, 2)
                .apply("date_format(reply_time, '%Y-%m-%d' ) = '" + dateTime.toDateStr() + "'"))).orElseGet(ArrayList::new);

        List<WeQiRuleMsg> timeOutList = Optional.ofNullable(weQiRuleMsgService.list(new LambdaQueryWrapper<WeQiRuleMsg>()
                .eq(WeQiRuleMsg::getReceiveId, userId)
                .in(WeQiRuleMsg::getStatus, ListUtil.toList(1, 2))
                .apply("date_format(reply_time, '%Y-%m-%d' ) = '" + dateTime.toDateStr() + "'"))).orElseGet(ArrayList::new);

        //成员回复次数
        statistics.setReplyNum(String.valueOf(replyNumList.size()));

        //成员超时次数
        statistics.setTimeOutNum(String.valueOf(timeOutList.size()));

        //成员超时率 【成员超时次数】/【成员回复次数】的百分比
        if (replyNumList.size() == 0 && timeOutList.size() != 0) {
            statistics.setTimeOutRate("100");
        } else if (replyNumList.size() == 0 && timeOutList.size() == 0) {
            statistics.setTimeOutRate("0");
        } else {
            BigDecimal timeOutRate = new BigDecimal(timeOutList.size() / replyNumList.size()).setScale(2, BigDecimal.ROUND_HALF_UP);
            statistics.setTimeOutRate(timeOutRate.toString());
        }

        //客户会话超时率 【成员超时次数】/【成员回复次数】的百分比（仅计算其中属于客户会话的次数）
        long customerReplyNum = replyNumList.stream().filter(item -> Objects.equals(1, item.getChatType())).count();
        long customerTimeOutNum = timeOutList.stream().filter(item -> Objects.equals(1, item.getChatType())).count();
        if (customerReplyNum == 0L && customerTimeOutNum != 0L) {
            statistics.setChatTimeOutRate("100");
        } else if (customerReplyNum == 0L && customerTimeOutNum == 0L) {
            statistics.setChatTimeOutRate("0");
        } else {
            BigDecimal chatTimeOutRate = new BigDecimal(customerTimeOutNum / customerReplyNum).setScale(2, BigDecimal.ROUND_HALF_UP);
            statistics.setChatTimeOutRate(chatTimeOutRate.toString());
        }


        //客群回话超时率 【成员超时次数】/【成员回复次数】的百分比（仅计算其中属于客群会话的次数）
        long groupReplyNum = replyNumList.stream().filter(item -> Objects.equals(2, item.getChatType())).count();
        long groupTimeOutNum = timeOutList.stream().filter(item -> Objects.equals(2, item.getChatType())).count();
        if (groupReplyNum == 0L && groupTimeOutNum != 0L) {
            statistics.setChatTimeOutRate("100");
        } else if (groupReplyNum == 0L && groupTimeOutNum == 0L) {
            statistics.setChatTimeOutRate("0");
        } else {
            BigDecimal groupChatTimeOutRate = new BigDecimal(groupTimeOutNum / groupReplyNum).setScale(2, BigDecimal.ROUND_HALF_UP);
            statistics.setGroupChatTimeOutRate(groupChatTimeOutRate.toString());
        }

        weQiRuleUserStatisticsService.saveOrUpdate(statistics, new LambdaQueryWrapper<WeQiRuleUserStatistics>()
                .eq(WeQiRuleUserStatistics::getWeUserId, userId)
                .apply("date_format(state_time, '%Y-%m-%d' ) = '" + dateTime.toDateStr() + "'"));
    }


    @Data
    private static class WeChatMsgQiRuleWeeklyTaskParams {
        private String userIds;
        private String startTime;
        private String endTime;
    }
}
