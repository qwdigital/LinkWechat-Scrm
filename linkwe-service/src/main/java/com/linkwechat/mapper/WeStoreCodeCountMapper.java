package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.storecode.entity.WeStoreCode;
import com.linkwechat.domain.storecode.entity.WeStoreCodeCount;
import com.linkwechat.domain.storecode.query.WeStoreCodeQuery;
import com.linkwechat.domain.storecode.vo.WeStoreCodeTableVo;
import com.linkwechat.domain.storecode.vo.datareport.WeStoreGroupReportVo;
import com.linkwechat.domain.storecode.vo.datareport.WeStoreShopGuideReportVo;
import com.linkwechat.domain.storecode.vo.drum.WeStoreGroupDrumVo;
import com.linkwechat.domain.storecode.vo.drum.WeStoreShopGuideDrumVo;
import com.linkwechat.domain.storecode.vo.tab.WeStoreGroupTabVo;
import com.linkwechat.domain.storecode.vo.tab.WeStoreShopGuideTabVo;
import com.linkwechat.domain.storecode.vo.tab.WeStoreTabVo;
import com.linkwechat.domain.storecode.vo.trend.WeStoreGroupTrendVo;
import com.linkwechat.domain.storecode.vo.trend.WeStoreShopGuideTrendVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeStoreCodeCountMapper extends BaseMapper<WeStoreCodeCount> {

    /**
     * 门店导购码统计-头部tab
     *
     * @return
     */

    WeStoreShopGuideTabVo countWeStoreShopGuideTab(@Param("state") String state);

    /**
     * 门店统计-头部tab
     *
     * @param storeCodeId
     * @param groupCodeId
     * @return
     */
    WeStoreTabVo countWeStoreTab(@Param("storeCodeId") Long storeCodeId);


    /**
     * 门店群活码统计-头部tab
     *
     * @return
     */
    WeStoreGroupTabVo countWeStoreGroupTab(@Param("state") String state);


    /**
     * 门店导购码统计-门店新增客户top10
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    List<WeStoreShopGuideDrumVo> countStoreShopGuideDrum(@Param("beginTime") String beginTime, @Param("endTime") String endTime);


    /**
     * 门店群码统计-门店客群新增客户top10
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    List<WeStoreGroupDrumVo> countStoreShopGroupDrum(@Param("beginTime") String beginTime, @Param("endTime") String endTime);


    /**
     * 门店导购统计-数据报表
     *
     * @param weStoreCode
     * @return
     */
    List<WeStoreShopGuideReportVo> countShopGuideReport(@Param("weStoreCode") WeStoreCode weStoreCode);


    /**
     * 门店群码统计-数据报表
     *
     * @param weStoreCode
     * @return
     */
    List<WeStoreGroupReportVo> countStoreGroupReport(@Param("weStoreCode") WeStoreCode weStoreCode);


    /**
     * 门店导购码趋势图
     *
     * @param weStoreCode
     * @return
     */
    List<WeStoreShopGuideTrendVo> countStoreShopGuideTrend(@Param("weStoreCode") WeStoreCode weStoreCode);


    /**
     * 门店群码趋势图
     *
     * @param weStoreCode
     * @return
     */
    List<WeStoreGroupTrendVo> countStoreGroupTrend(@Param("weStoreCode") WeStoreCode weStoreCode);


    /**
     * 获取数据明细
     * @param weStoreCodeQuery
     * @return
     */
    List<WeStoreCodeTableVo> findWeStoreCodeTables(@Param("query") WeStoreCodeQuery weStoreCodeQuery);

}
