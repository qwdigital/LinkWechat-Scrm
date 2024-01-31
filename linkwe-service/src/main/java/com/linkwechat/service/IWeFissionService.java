package com.linkwechat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.WeGroupMember;
import com.linkwechat.domain.fission.WeFission;
import com.linkwechat.domain.fission.WeFissionInviterPoster;
import com.linkwechat.domain.fission.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author robin
* @description 针对表【we_fission(裂变（任务宝,群裂变）)】的数据库操作Service
* @createDate 2023-03-14 14:07:21
*/
public interface


IWeFissionService extends IService<WeFission> {


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
     * 裂变明细(群裂变)
     * @param fissionId
     * @param customerName
     * @param weUserId
     * @return
     */
    List<WeGroupFissionDetailVo> findWeGroupFissionDetail(String fissionId,String customerName, String weUserId,String chatId);


    /**
     * 裂变明细(任务宝)
     * @param fissionId
     * @param customerName
     * @param weUserId
     * @return
     */
    List<WeTaskFissionDetailVo> findWeTaskFissionDetail(String fissionId,String customerName,String weUserId);


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
    WeFissionInviterPoster findFissionPoster(String unionid, String fissionId) throws Exception;


    /**
     * 裂变要成功处理逻辑(任务宝)
     * @param state 渠道标识
     * @param weCustomer 邀请的客户
     */
    void handleTaskFissionRecord(String state,WeCustomer weCustomer);


    /**
     * 裂变要成功处理逻辑(群裂变)
     * @param state 渠道标识
     * @param weGroupMember 邀请进群的成员
     */
    void handleGroupFissionRecord(String state, WeGroupMember weGroupMember);


    /**
     * 裂变处理(状态维护，过期状态维护)
     */
    void handleFission();

    /**
     * 过期列变
     */
    void handleExpireFission();


    /**
     * 更新状态为未发送
     * @param weFissions
     */
    void updateBatchFissionIsTipNoSend(List<WeFission> weFissions);



    void getXX();
}
