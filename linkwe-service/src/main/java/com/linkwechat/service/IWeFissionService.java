package com.linkwechat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.fission.WeFission;
import com.linkwechat.domain.fission.WeFissionInviterPoster;
import com.linkwechat.domain.fission.vo.*;
import java.util.List;

/**
* @author robin
* @description 针对表【we_fission(裂变（任务宝,群裂变）)】的数据库操作Service
* @createDate 2023-03-14 14:07:21
*/
public interface IWeFissionService extends IService<WeFission> {


    /**
     * 构建裂变
     * @param weFission
     */
    void buildWeFission(WeFission weFission);



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
    List<WeGroupFissionDetailVo> findWeGroupFissionDetail(String customerName, String weUserId,String chatId);


    /**
     * 裂变明细sub
     * @param fissionInviterRecordId
     * @return
     */
    List<WeFissionDetailSubVo> findWeFissionDetailSub(Long fissionInviterRecordId);


    /**
     * 获取裂变海报
     * @param unionid
     * @param fissionId
     * @return
     */
    WeFissionInviterPoster findFissionPoster(String unionid, String fissionId);
}
