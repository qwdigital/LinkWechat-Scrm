package com.linkwechat.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.operation.query.WeOperationCustomerQuery;
import com.linkwechat.domain.operation.query.WeOperationGroupQuery;
import com.linkwechat.domain.operation.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author danmo
 * @description 运营中心mapper
 * @date 2022/1/9 17:07
 **/

@Mapper
public interface WeOperationCenterMapper {

    /**
     * 客户分析
     * @return
     */
    WeCustomerAnalysisVo getCustomerAnalysis();


    /**
     * 自建应用客户分析统计
     * @param weUserIds
     * @return
     */
    WeCustomerAnalysisVo getCustomerAnalysisForApp(@Param("weUserIds") List<String> weUserIds);

    /**
     * 客户总数
     * @param query
     * @return
     */
    List<WeCustomerTotalCntVo> getCustomerTotalCnt(WeOperationCustomerQuery query);

    /**
     * 客户实时数据
     * @param query
     * @return
     */
    List<WeCustomerRealCntVo> getCustomerRealCnt(WeOperationCustomerQuery query);
    /**
     * 客户实时流失数据
     * @param query
     * @return
     */
    List<WeCustomerRealCntVo> getCustomerLostCnt(WeOperationCustomerQuery query);

    /**
     * 客户实时流失数据(非连续性数据)
     * @param query
     * @return
     */
    List<WeCustomerRealCntVo> getNewCustomerLostCnt(WeOperationCustomerQuery query);

    /**
     * 员工客户排行
     * @param query
     * @return
     */
    List<WeUserCustomerRankVo> getCustomerRank(WeOperationCustomerQuery query);

    /**
     * 客群分析
     * @return
     */
    WeGroupAnalysisVo getGroupAnalysis();


    /**
     * 自建应用，客群分析
     * @param chatIds
     * @return
     */
    WeGroupAnalysisVo getGroupAnalysisByApp(@Param("chatIds") List<String> chatIds);

    /**
     * 客群总数
     * @param query
     * @return
     */
    List<WeGroupTotalCntVo> getGroupTotalCnt(WeOperationGroupQuery query);

    /**
     * 客群成员总数
     * @param query
     * @return
     */
    List<WeGroupTotalCntVo> getGroupMemberTotalCnt(WeOperationGroupQuery query);

    /**
     * 客群实时数据
     * @param query
     * @return
     */
    List<WeGroupRealCntVo> getGroupRealCnt(WeOperationGroupQuery query);

    /**
     * 客群实时数据
     * @param query
     * @return
     */
    List<WeGroupMemberRealCntVo> getGroupMemberRealCnt(WeOperationGroupQuery query);

    /**
     * 会话分析-客户联系总数
     * @return
     */
    WeSessionCustomerAnalysisVo getCustomerSessionAnalysis();

    /**
     * 会话分析-客户会话总数
     * @param query
     * @return
     */
    List<WeSessionCustomerTotalCntVo> getCustomerSessionTotalCnt(WeOperationCustomerQuery query);

    /**
     * 员工单聊总数
     * @param query
     * @return
     */
    List<WeSessionUserChatRankVo> getUserChatRank(WeOperationCustomerQuery query);

    /**
     * 员工平均恢复时长
     * @param query
     * @return
     */
    List<WeSessionUserAvgReplyTimeRankVo> getUserAvgReplyTimeRank(WeOperationCustomerQuery query);

    /**
     * 会话分析-客群联系总数
     * @return
     */
    WeSessionGroupAnalysisVo getGroupSessionAnalysis();

    /**
     * 会话分析-群聊会话总数
     * @param query
     * @return
     */
    List<WeSessionGroupTotalCntVo> getGroupSessionTotalCnt(WeOperationGroupQuery query);

    /**
     * 会话存档数据分析
     * @return
     */
    WeSessionArchiveAnalysisVo getSessionArchiveAnalysis();

    /**
     * 会话存档同意明细
     * @return
     */
    List<WeSessionArchiveDetailVo> getSessionArchiveDetails(BaseEntity query);

    /**
     * 每日提醒客户数据分析
     * @return
     */
    WeCustomerRemindAnalysisVo findWeCustomerRemindAnalysis();


    /**
     * 每日提醒群数据分析
     * @return
     */
    WeGroupRemindAnalysisVo findWeGroupRemindAnalysis();


    /**
     * 客户群成员折线数据(按照时间段查询,不间断)
     * @param query
     * @return
     */
    List<WeGroupMemberRealCntVo> selectGroupMemberBrokenLine(WeOperationGroupQuery query);
}
