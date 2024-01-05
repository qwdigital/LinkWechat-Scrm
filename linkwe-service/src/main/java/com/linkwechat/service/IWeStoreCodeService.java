package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.storecode.entity.WeStoreCode;
import com.linkwechat.domain.storecode.entity.WeStoreCodeCount;
import com.linkwechat.domain.storecode.query.WeStoreCodeQuery;
import com.linkwechat.domain.storecode.query.WxStoreCodeQuery;
import com.linkwechat.domain.storecode.vo.WeStoreCodeTableVo;
import com.linkwechat.domain.storecode.vo.WeStoreCodesVo;
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

public interface IWeStoreCodeService extends IService<WeStoreCode> {


    /**
     * 获取门店活码
     * @param weStoreCode
     * @return
     */
   List<WeStoreCode> storeCodes(WeStoreCode weStoreCode);

    /**
     * 新增或更新门店导购码
     * @param weStoreCode
     */
    void createOrUpdateStoreCode(WeStoreCode weStoreCode);


    /**
     * 门店导购码统计-头部tab
     * @return
     */
    WeStoreShopGuideTabVo countWeStoreShopGuideTab();


    /**
     * 门店码统计-头部tab
     * @param storeCodeId
     * @return
     */
    WeStoreTabVo countWeStoreTab(Long storeCodeId);


    /**
     *  门店群活码统计-头部tab
     * @return
     */
    WeStoreGroupTabVo countWeStoreGroupTab();


    /**
     * 门店导购码趋势图
     * @param weStoreCode
     * @return
     */
    List<WeStoreShopGuideTrendVo> countStoreShopGuideTrend(WeStoreCode weStoreCode);


    /**
     *  门店群码趋势图
     * @param weStoreCode
     * @return
     */
    List<WeStoreGroupTrendVo> countStoreGroupTrend(WeStoreCode weStoreCode);


    /**
     *  门店导购码统计-门店新增客户top10
     * @param beginTime
     * @param endTime
     * @return
     */
    List<WeStoreShopGuideDrumVo> countStoreShopGuideDrum(String beginTime,String endTime);




    /**
     *  门店群码统计-门店客群新增客户top10
     * @param beginTime
     * @param endTime
     * @return
     */
    List<WeStoreGroupDrumVo> countStoreShopGroupDrum(String beginTime,String endTime);




    /**
     * 门店导购统计-数据报表
     * @param weStoreCode
     * @return
     */
    List<WeStoreShopGuideReportVo> countShopGuideReport(WeStoreCode weStoreCode);



    /**
     * 门店群码统计-数据报表
     * @param weStoreCode
     * @return
     */
    List<WeStoreGroupReportVo> countStoreGroupReport(WeStoreCode weStoreCode);


    /**
     * 根据定位获取门店
     * @param wxStoreCodeQuery
     * @return
     */
    WeStoreCodesVo findStoreCode(WxStoreCodeQuery wxStoreCodeQuery);


    /**
     * 记录用户扫码行为
     * @param wxStoreCodeQuery
     */
    void countUserBehavior(WxStoreCodeQuery wxStoreCodeQuery);


  /**
   * 数据明细获取
   * @param weStoreCodeQuery
   * @return
   */
  List<WeStoreCodeTableVo> findWeStoreCodeTables(WeStoreCodeQuery weStoreCodeQuery);


 /**
  * 批量更新状态
  * @param storeState
  * @param ids
  */
  void batchUpdateState(Integer storeState,List<Long> ids);
}
