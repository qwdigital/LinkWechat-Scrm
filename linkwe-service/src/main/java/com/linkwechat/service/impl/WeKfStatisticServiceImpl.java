package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.common.core.page.TableSupport;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.kf.query.WeKfCustomerStatisticQuery;
import com.linkwechat.domain.kf.vo.*;
import com.linkwechat.mapper.WeKfStatisticMapper;
import com.linkwechat.service.IWeKfStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
