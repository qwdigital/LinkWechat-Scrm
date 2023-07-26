package com.linkwechat.scheduler.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.enums.WeKfOriginEnum;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.*;
import com.linkwechat.service.*;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 客服统计任务
 * @date 2022/11/27 10:41
 **/
@Slf4j
@Component
public class WeKfStatTask {


    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Autowired
    private IWeKfPoolService weKfPoolService;

    @Autowired
    private IWeKfCustomerStatService weKfCustomerStatService;

    @Autowired
    private IWeKfUserStatService weKfUserStatService;

    @Autowired
    private IWeKfMsgService weKfMsgService;

    @Autowired
    private IWeKfInfoService weKfInfoService;

    @XxlJob("weKfCustomerStatTask")
    public void customerProcess(String params) {
        log.info("客服客户统计任务>>>>>>>>>>>>>>>>>>>启动 params:{}", params);
        List<WeCorpAccount> accountList = weCorpAccountService.getAllCorpAccountInfo();

        if (CollectionUtil.isNotEmpty(accountList)) {
            for (WeCorpAccount corpAccountInfo : accountList) {
                ThreadUtil.execute(() -> getKfCustomerStat(corpAccountInfo, params));
            }
        }
    }

    @XxlJob("weKfUserStatTask")
    public void userProcess(String params) {
        log.info("客服员工统计任务>>>>>>>>>>>>>>>>>>>启动 params:{}", params);
        List<WeCorpAccount> accountList = weCorpAccountService.getAllCorpAccountInfo();
        if (CollectionUtil.isNotEmpty(accountList)) {
            for (WeCorpAccount corpAccountInfo : accountList) {
                ThreadUtil.execute(() -> getKfUserStat(corpAccountInfo, params));
            }
        }
    }

    private void getKfCustomerStat(WeCorpAccount weCorpAccount, String params) {
        log.info("客服客户当前统计租户为 tenantId:{},params:{}", weCorpAccount.getId(),params);
        String dateStr = StringUtils.isNotBlank(params)?params:DateUtil.yesterday().toDateStr();
        WeKfCustomerStat kfCustomerStat = weKfPoolService.getKfCustomerStat(dateStr);
        kfCustomerStat.setDateTime(dateStr);
        weKfCustomerStatService.save(kfCustomerStat);
    }

    private void getKfUserStat(WeCorpAccount weCorpAccount, String params) {
        log.info("客服员工当前统计租户为 tenantId:{},params:{}", weCorpAccount.getId(),params);
        String dateTime = StringUtils.isNotBlank(params)?params:DateUtil.yesterday().toDateStr();
        List<WeKfUserStat> kfUserStat = weKfPoolService.getKfUserStat(dateTime);

        //查询当天会话数据
        Map<String, List<WeKfUserStat>> recordData = getRecordData(weCorpAccount, dateTime);
        if(CollectionUtil.isNotEmpty(kfUserStat)){
            for (WeKfUserStat userStat : kfUserStat) {
                userStat.setDateTime(dateTime);
              if(recordData.containsKey(userStat.getOpenKfId()+"#"+userStat.getUserId())){
                  List<WeKfUserStat> weKfUserStats = recordData.get(userStat.getOpenKfId()+"#"+userStat.getUserId());
                  int talkCnt = weKfUserStats.stream().filter(item -> Objects.nonNull(item.getTalkCnt())).mapToInt(WeKfUserStat::getTalkCnt).sum();
                  userStat.setTalkCnt(talkCnt);
                  int timeOutCnt = weKfUserStats.stream().filter(item -> Objects.nonNull(item.getTimeOutCnt())).mapToInt(WeKfUserStat::getTimeOutCnt).sum();
                  userStat.setTimeOutCnt(timeOutCnt);
                  long timeOutDuration = weKfUserStats.stream().filter(item -> Objects.nonNull(item.getTimeOutDuration())).mapToLong(WeKfUserStat::getTimeOutDuration).sum();
                  userStat.setTimeOutDuration(timeOutDuration);
              }
            }
        }
        weKfUserStatService.saveBatch(kfUserStat,500);
    }

    private Map<String, List<WeKfUserStat>> getRecordData(WeCorpAccount weCorpAccount, String dateTime) {
        List<WeKfPool> kfPoolList = weKfPoolService.list(new LambdaQueryWrapper<WeKfPool>()
                .isNotNull(WeKfPool::getSessionStartTime)
                .apply("DATE_FORMAT(CREATE_TIME, '%Y-%m-%d' ) = '" + dateTime + "'"));
        Map<String, List<WeKfUserStat>> userStatMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(kfPoolList)){
            Map<String, List<WeKfPool>> userPoolMap = kfPoolList.stream().collect(Collectors.groupingBy(item -> item.getOpenKfId()+"#"+ item.getUserId()));
            userPoolMap.forEach((openKfIdAndUserId,poolList) ->{
                List<WeKfUserStat> weKfUserStatsList = new LinkedList<>();
                for (WeKfPool weKfPool : poolList) {
                    WeKfUserStat stat = new WeKfUserStat();
                    WeKfInfo weKfInfo = weKfInfoService.getOne(new LambdaQueryWrapper<WeKfInfo>().eq(WeKfInfo::getCorpId,weCorpAccount.getCorpId()).eq(WeKfInfo::getOpenKfId,weKfPool.getOpenKfId()).last("limit 1"));
                    Integer timeOutNotice = weKfInfo.getTimeOutNotice();
                    Integer timeOutType = weKfInfo.getTimeOutType();
                    Integer timeOut = weKfInfo.getTimeOut();

                    List<WeKfMsg> weKfMsgList = weKfMsgService.list(new LambdaQueryWrapper<WeKfMsg>()
                            .eq(WeKfMsg::getOpenKfId, weKfPool.getOpenKfId())
                            .eq(WeKfMsg::getExternalUserid, weKfPool.getExternalUserId())
                            .ge(WeKfMsg::getSendTime, DateUtil.formatDateTime(weKfPool.getEnterTime()))
                            .le(WeKfMsg::getSendTime, DateUtil.formatDateTime(weKfPool.getSessionEndTime()))
                            .orderByAsc(WeKfMsg::getSendTime)
                    );
                    //计算回复聊天对
                    if (CollectionUtil.isNotEmpty(weKfMsgList)) {
                        int i = 0;
                        int j = 1;
                        int outTimeCnt = 0;
                        int count = 0;
                        long durationCnt = 0L;
                        while (j < weKfMsgList.size()) {
                            if (Objects.equals(WeKfOriginEnum.CUSTOMER_SEND.getType(), weKfMsgList.get(i).getOrigin())
                                    && Objects.equals(WeKfOriginEnum.SERVICER_SEND.getType(), weKfMsgList.get(j).getOrigin())) {
                                long duration = 0;
                                if (ObjectUtil.equal(1, timeOutType)) {
                                    duration = DateUtil.between(weKfMsgList.get(i).getSendTime(), weKfMsgList.get(j).getSendTime(), DateUnit.MINUTE);
                                } else {
                                    duration = DateUtil.between(weKfMsgList.get(i).getSendTime(), weKfMsgList.get(j).getSendTime(), DateUnit.HOUR);
                                }
                                if (timeOut < duration) {
                                    outTimeCnt++;
                                    long minute = DateUtil.between(weKfMsgList.get(i).getSendTime(), weKfMsgList.get(j).getSendTime(), DateUnit.SECOND);
                                    durationCnt += minute;
                                }

                                count++;
                            }
                            i++;
                            j++;
                        }
                        stat.setTalkCnt(count);
                        stat.setTimeOutCnt(outTimeCnt);
                        stat.setTimeOutDuration(durationCnt);
                    }
                    weKfUserStatsList.add(stat);
                }
                userStatMap.put(openKfIdAndUserId,weKfUserStatsList);
            });
        }
        return userStatMap;
    }
}
