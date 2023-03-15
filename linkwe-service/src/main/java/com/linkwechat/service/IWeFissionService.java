package com.linkwechat.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.fission.WeFission;
import com.linkwechat.domain.fission.vo.*;
import java.util.List;

/**
* @author robin
* @description 针对表【we_fission(裂变（任务宝,群裂变）)】的数据库操作Service
* @createDate 2023-03-14 14:07:21
*/
public interface IWeFissionService extends IService<WeFission> {
    /**
     * 裂变列表
     * @param weFission
     * @return
     */
    List<WeFission> findWeFissions(WeFission weFission);

    /**
     * 裂变头部汇总统计
     * @param fissionId
     * @return
     */
    WeFissionTabVo findWeFissionTab(Long fissionId);


    /**
     * 数据趋势
     * @param weFission
     * @return
     */
    List<WeFissionTrendVo> findWeFissionTrend(WeFission weFission);


    /**
     * 数据报表
     * @param weFission
     * @return
     */
    List<WeFissionDataReportVo> findWeFissionDataReport(WeFission weFission);


    /**
     * 裂变明细
     * @param customerName
     * @param weUserId
     * @return
     */
    List<WeFissionDetailVo> findWeFissionDetail(String customerName,String weUserId);


    /**
     * 裂变明细sub
     * @param fissionDetailId
     * @return
     */
    List<WeFissionDetailSubVo> findWeFissionDetailSub(Long fissionDetailId);
}
