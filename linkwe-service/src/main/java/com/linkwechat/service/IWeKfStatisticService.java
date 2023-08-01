package com.linkwechat.service;

import com.github.pagehelper.PageInfo;
import com.linkwechat.domain.kf.query.WeKfCustomerStatisticQuery;
import com.linkwechat.domain.kf.query.WeKfQualityStatQuery;
import com.linkwechat.domain.kf.vo.*;

import java.util.List;

/**
 * @author danmo
 * @description 客服统计业务接口
 * @date 2022/1/9 17:02
 **/
public interface IWeKfStatisticService {
    /**
     * 场景客户分析
     *
     * @return
     */
    WeKfSceneAnalysisVo getSceneCustomerAnalysis();

    /**
     * 场景客户实时数据
     * @return
     */
    List<WeKfSceneRealCntVo> getSceneCustomerRealCnt(WeKfCustomerStatisticQuery query);

    /**
     * 场景值排行
     * @param query
     * @return
     */
    WeKfSceneRankCntListVo getSceneCustomerRank(WeKfCustomerStatisticQuery query);

    /**
     * 场景客户实时数据（分页）
     * @param query
     * @return
     */
    List<WeKfSceneRealCntVo> getSceneCustomerRealPageCnt(WeKfCustomerStatisticQuery query);

    /**
     * 咨询客户数据分析
     * @return
     */
    WeKfConsultAnalysisVo getConsultCustomerAnalysis();

    /**
     * 接待人员排行
     * @param query
     * @return
     */
    WeKfConsultRankCntListVo getConsultUserRank(WeKfCustomerStatisticQuery query);

    /**
     * 咨询客户实时数据
     * @param query
     * @return
     */
    List<WeKfConsultRealCntVo> getConsultCustomerRealCnt(WeKfCustomerStatisticQuery query);

    /**
     * 咨询客户实时数据（分页）
     * @param query
     * @return
     */
    List<WeKfConsultRealCntVo> getConsultCustomerRealPageCnt(WeKfCustomerStatisticQuery query);

    /**
     * 质量分析数据
     * @return
     */
    WeKfQualityAnalysisVo getQualityAnalysis();

    /**
     * 质量分析数据-折线图
     * @param query
     * @return
     */
    List<WeKfQualityBrokenLineVo> getQualityBrokenLine(WeKfQualityStatQuery query);

    /**
     * 质量分析数据-柱状图
     * @param query
     * @return
     */
    List<WeKfQualityHistogramVo> getQualityHistogram(WeKfQualityStatQuery query);

    /**
     * 质量分析数据-图表
     * @param query
     * @return
     */
    PageInfo<WeKfQualityChatVo> getQualityChart(WeKfQualityStatQuery query);
}
