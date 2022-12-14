package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.common.core.page.TableSupport;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.WeKfCustomerStat;
import com.linkwechat.domain.WeKfUserStat;
import com.linkwechat.domain.kf.query.WeKfCustomerStatisticQuery;
import com.linkwechat.domain.kf.query.WeKfQualityStatQuery;
import com.linkwechat.domain.kf.vo.*;
import com.linkwechat.mapper.WeKfStatisticMapper;
import com.linkwechat.service.IWeKfCustomerStatService;
import com.linkwechat.service.IWeKfStatisticService;
import com.linkwechat.service.IWeKfUserStatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 客服统计业务实现类
 * @date 2022/2/9 0:03
 **/
@Slf4j
@Service
public class WeKfStatisticServiceImpl implements IWeKfStatisticService {

    @Autowired
    private WeKfStatisticMapper weKfStatisticMapper;

    @Autowired
    private IWeKfCustomerStatService weKfCustomerStatService;

    @Autowired
    private IWeKfUserStatService weKfUserStatService;

    @Override
    public WeKfSceneAnalysisVo getSceneCustomerAnalysis() {
        return weKfStatisticMapper.getSceneCustomerAnalysis();
    }

    @Override
    public List<WeKfSceneRealCntVo> getSceneCustomerRealCnt(WeKfCustomerStatisticQuery query) {
        List<WeKfSceneRealCntVo> sceneCustomerRealCnt = weKfStatisticMapper.getSceneCustomerRealCnt(query);
        return sceneCustomerRealCnt;
    }


    @Override
    public WeKfSceneRankCntListVo getSceneCustomerRank(WeKfCustomerStatisticQuery query) {
        WeKfSceneRankCntListVo rankCntListVo = new WeKfSceneRankCntListVo();
        rankCntListVo.setVisit(weKfStatisticMapper.getSceneCustomerVisitRank(query));
        rankCntListVo.setConsult(weKfStatisticMapper.getSceneCustomerConsultRank(query));
        return rankCntListVo;
    }

    @Override
    public List<WeKfSceneRealCntVo> getSceneCustomerRealPageCnt(WeKfCustomerStatisticQuery query) {
        List<WeKfSceneRealCntVo> sceneCustomerRealCnt = weKfStatisticMapper.getSceneCustomerRealPageCnt(query);
        return sceneCustomerRealCnt;
    }

    @Override
    public WeKfConsultAnalysisVo getConsultCustomerAnalysis() {
        return weKfStatisticMapper.getConsultCustomerAnalysis();
    }

    @Override
    public WeKfConsultRankCntListVo getConsultUserRank(WeKfCustomerStatisticQuery query) {
        WeKfConsultRankCntListVo rankCntListVo = new WeKfConsultRankCntListVo();
        rankCntListVo.setReply(weKfStatisticMapper.getConsultUserReplyRank(query));
        rankCntListVo.setAvgReplyDuration(weKfStatisticMapper.getConsultUserAvgReplyDurationRank(query));
        return rankCntListVo;
    }

    @Override
    public List<WeKfConsultRealCntVo> getConsultCustomerRealCnt(WeKfCustomerStatisticQuery query) {
        List<WeKfConsultRealCntVo> replyRealCnt = weKfStatisticMapper.getReplyRealCnt(query);
        List<WeKfSceneRealCntVo> customerRealCnt = weKfStatisticMapper.getSceneCustomerRealCnt(query);
        Map<String, List<WeKfSceneRealCntVo>> realCntMap = customerRealCnt.stream().collect(Collectors.groupingBy(WeKfSceneRealCntVo::getXTime));
        replyRealCnt.forEach(replyReal ->{
            List<WeKfSceneRealCntVo> realCntVos = realCntMap.get(replyReal.getXTime());
            if(CollectionUtil.isNotEmpty(realCntVos)){
                WeKfSceneRealCntVo realCntVo = realCntVos.get(0);
                replyReal.setConsultTotalCnt(realCntVo.getConsultTotalCnt());
                replyReal.setReceptionTotalCnt(realCntVo.getReceptionTotalCnt());
            }
        });
        return replyRealCnt;
    }

    @Override
    public List<WeKfConsultRealCntVo> getConsultCustomerRealPageCnt(WeKfCustomerStatisticQuery query) {
        List<WeKfConsultRealCntVo> replyRealCnt = weKfStatisticMapper.getReplyRealPageCnt(query);
        PageDomain pageDomain = TableSupport.buildPageRequest();
        PageHelper.startPage(pageDomain.getPageNum(),pageDomain.getPageSize());
        List<WeKfSceneRealCntVo> customerRealCnt = weKfStatisticMapper.getSceneCustomerRealPageCnt(query);
        Map<String, List<WeKfSceneRealCntVo>> realCntMap = customerRealCnt.stream().collect(Collectors.groupingBy(WeKfSceneRealCntVo::getXTime));

        PageHelper.startPage(pageDomain.getPageNum(),pageDomain.getPageSize());
        List<WeKfConsultDurationVo> replyRealDurationCnt = weKfStatisticMapper.getReplyRealDurationCnt(query);
        Map<String, List<WeKfConsultDurationVo>> durationCntMap = replyRealDurationCnt.stream().collect(Collectors.groupingBy(WeKfConsultDurationVo::getXTime));

        replyRealCnt.forEach(replyReal ->{
            List<WeKfSceneRealCntVo> realCntVos = realCntMap.get(replyReal.getXTime());
            if(CollectionUtil.isNotEmpty(realCntVos)){
                WeKfSceneRealCntVo realCntVo = realCntVos.get(0);
                replyReal.setConsultTotalCnt(realCntVo.getConsultTotalCnt());
                replyReal.setReceptionTotalCnt(realCntVo.getReceptionTotalCnt());
            }
            List<WeKfConsultDurationVo> durationCntVos = durationCntMap.get(replyReal.getXTime());
            if(CollectionUtil.isNotEmpty(durationCntVos)){
                WeKfConsultDurationVo durationCntVo = durationCntVos.get(0);
                replyReal.setAvgConsultDuration(durationCntVo.getAvgConsultDuration());
                replyReal.setAvgConsultReplyDuration(durationCntVo.getAvgConsultReplyDuration());
            }
        });
        return replyRealCnt;
    }

    @Override
    public WeKfQualityAnalysisVo getQualityAnalysis() {
        WeKfQualityAnalysisVo weKfQualityAnalysisVo = new WeKfQualityAnalysisVo();
        List<WeKfCustomerStat> customerStatList = weKfCustomerStatService.list();
        NumberFormat fmt = NumberFormat.getPercentInstance();
        if(CollectionUtil.isNotEmpty(customerStatList)){
            int evaluateCnt = customerStatList.stream().mapToInt(WeKfCustomerStat::getEvaluateCnt).sum();
            weKfQualityAnalysisVo.setEvaluateCnt(evaluateCnt);
            int sessionCnt = customerStatList.stream().mapToInt(WeKfCustomerStat::getSessionCnt).sum();
            //参评率
            float evaluateAsr = (float) evaluateCnt / sessionCnt;
            if (!Float.isNaN(evaluateAsr)) {
                weKfQualityAnalysisVo.setEvaluateRate(fmt.format(evaluateAsr));
            }
            //好评率
            int goodCnt = customerStatList.stream().mapToInt(WeKfCustomerStat::getGoodCnt).sum();
            float goodAsr = (float) goodCnt / evaluateCnt;
            if (!Float.isNaN(goodAsr)) {
                weKfQualityAnalysisVo.setGoodRate(fmt.format(goodAsr));
            }
            //一般率
            int commonCnt = customerStatList.stream().mapToInt(WeKfCustomerStat::getCommonCnt).sum();
            float commonAsr = (float) commonCnt / evaluateCnt;
            if (!Float.isNaN(commonAsr)) {
                weKfQualityAnalysisVo.setCommonRate(fmt.format(commonAsr));
            }
            //差评率
            int badCnt = customerStatList.stream().mapToInt(WeKfCustomerStat::getBadCnt).sum();
            float badAsr = (float) badCnt / evaluateCnt;
            if (!Float.isNaN(badAsr)) {
                weKfQualityAnalysisVo.setBadRate(fmt.format(badAsr));
            }
        }
        List<WeKfUserStat> userStatList = weKfUserStatService.list();
        if(CollectionUtil.isNotEmpty(userStatList)){
            //超时率
            int talkCnt = userStatList.stream().mapToInt(WeKfUserStat::getTalkCnt).sum();
            int timeOutCnt = userStatList.stream().mapToInt(WeKfUserStat::getTimeOutCnt).sum();
            float timeOutAsr = (float) timeOutCnt / talkCnt;
            if (!Float.isNaN(timeOutAsr)) {
                weKfQualityAnalysisVo.setTimeOutRate(fmt.format(timeOutAsr));
            }
            //超时时长
            long timeOutDuration = userStatList.stream().mapToLong(WeKfUserStat::getTimeOutDuration).sum();
            float timeOutDurationAsr = (float) timeOutDuration / timeOutCnt;
            if (!Float.isNaN(timeOutDurationAsr)) {
                weKfQualityAnalysisVo.setLong2TimeOutDuration(BigDecimal.valueOf(timeOutDurationAsr).longValue());
            }
        }
        return weKfQualityAnalysisVo;
    }


    @Override
    public List<WeKfQualityBrokenLineVo> getQualityBrokenLine(WeKfQualityStatQuery query) {
        return weKfUserStatService.getQualityBrokenLine(query);
    }

    @Override
    public List<WeKfQualityHistogramVo> getQualityHistogram(WeKfQualityStatQuery query) {
        return weKfUserStatService.getQualityHistogram(query);
    }

    @Override
    public PageInfo<WeKfQualityChatVo> getQualityChart(WeKfQualityStatQuery query) {
        return weKfUserStatService.getQualityChart(query);
    }
}
