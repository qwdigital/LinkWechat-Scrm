package com.linkwechat.scheduler.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.thread.WeMsgQiRuleThreadExecutor;
import com.linkwechat.domain.*;
import com.linkwechat.service.*;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
    private IWeQiRuleScopeService weQiRuleScopeService;

    @Autowired
    private IWeChatContactMsgService weChatContactMsgService;

    @Autowired
    private IWeGroupService weGroupService;

    @Autowired
    private IWeQiRuleMsgService weQiRuleMsgService;

    @Autowired
    private IWeQiRuleUserStatisticsService weQiRuleUserStatisticsService;

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

        List<WeQiRuleScope> weQiRuleScopes = weQiRuleScopeService.list(new LambdaQueryWrapper<WeQiRuleScope>()
                .select(WeQiRuleScope::getUserId)
                .in(StringUtils.isNotEmpty(weeklyTaskParams.getUserIds()), WeQiRuleScope::getUserId, Arrays.stream(weeklyTaskParams.getUserIds().split(",")).collect(Collectors.toList()))
                .eq(WeQiRuleScope::getDelFlag, 0)
                .groupBy(WeQiRuleScope::getUserId));

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
        statistics.setStateTime(dateTime);
        //客户会话数
        int chatNum = weChatContactMsgService.count(new LambdaQueryWrapper<WeChatContactMsg>()
                .and(wrapper -> wrapper.eq(WeChatContactMsg::getFromId, userId)
                        .or().eq(WeChatContactMsg::getToList, userId))
                .apply("date_format(msg_time, '%Y-%m-%d' ) = '" + dateTime.toDateStr() + "'")
                .eq(WeChatContactMsg::getDelFlg, 0));
        statistics.setChatNum(String.valueOf(chatNum));

        //客群会话数
        List<WeGroup> groupList = Optional.ofNullable(weGroupService.list(new LambdaQueryWrapper<WeGroup>().select(WeGroup::getChatId).eq(WeGroup::getOwner, userId).eq(WeGroup::getDelFlag, 0))).orElseGet(ArrayList::new);

        int groupChatNum = weChatContactMsgService.count(new LambdaQueryWrapper<WeChatContactMsg>()
                .in(WeChatContactMsg::getRoomId, groupList.stream().map(WeGroup::getChatId).collect(Collectors.toList()))
                .apply("date_format(msg_time, '%Y-%m-%d' ) = '" + dateTime.toDateStr() + "'")
                .eq(WeChatContactMsg::getDelFlg, 0));
        statistics.setGroupChatNum(String.valueOf(groupChatNum));


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
