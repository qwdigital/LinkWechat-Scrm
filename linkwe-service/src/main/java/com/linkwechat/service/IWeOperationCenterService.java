package com.linkwechat.service;

import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.operation.query.WeOperationCustomerQuery;
import com.linkwechat.domain.operation.query.WeOperationGroupQuery;
import com.linkwechat.domain.operation.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author danmo
 * @description 运营中心业务接口
 * @date 2022/1/9 17:02
 **/
public interface IWeOperationCenterService {
    /**
     * 客户分析
     * @return
     */
    WeCustomerAnalysisVo getCustomerAnalysis();


    /**
     * 自建应用客户分析统计
     * @param dataScope 个人数据:false 全部数据(相对于角色定义的数据权限):true
     * @return
     */
    WeCustomerAnalysisVo getCustomerAnalysisForApp(boolean dataScope);


    /**
     * 自建应用，客群分析
     * @param dataScope 个人数据:false 全部数据(相对于角色定义的数据权限):true
     * @return
     */
    WeGroupAnalysisVo getGroupAnalysisByApp(boolean dataScope);



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
     * 员工客户排行统计
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

    List<WeCustomerRealCntVo> getCustomerRealCntPage(WeOperationCustomerQuery query);


    /**
     * 推送给个租户相关数据给租户
     */
    void pushData();

    /**
     * 客户群成员折线数据(按照时间段查询,不间断)
     * @param query
     * @return
     */
    List<WeGroupMemberRealCntVo> selectGroupMemberBrokenLine(WeOperationGroupQuery query);
}
