package com.linkwechat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.live.WeLive;
import com.linkwechat.domain.live.WeLiveTaskDetailTab;
import com.linkwechat.domain.live.WeLiveTaskUserDetail;
import com.linkwechat.domain.live.WeLiveTip;
import java.util.List;

/**
* @author robin
* @description 针对表【we_live_tip(直播员工群发通知消息表)】的数据库操作Service
* @createDate 2022-10-26 22:40:45
*/
public interface IWeLiveTipService extends IService<WeLiveTip> {

    /**
     * 构建消息
     * @param liveTips
     * @param weLive
     */
    void addOrUpdate(List<WeLiveTip> liveTips, WeLive weLive);


    /**
     * 获取提示任务
     * @param weLiveTip
     * @return
     */
    List<WeLiveTip> findWeLiveTip(WeLiveTip weLiveTip);


    /**
     * 分享统计-员工详情(客户或客群)
     * @param userName
     * @param liveId
     * @return
     */
    List<WeLiveTaskUserDetail> findWeLiveTaskUserDetail(String userName, String liveId, Integer sendTargetType);


    /**
     * 分享统计-客户详情(客户或客群)
     * @param sendTargetType
     * @param userName
     * @param liveId
     * @param sendState
     * @return
     */
    List<WeLiveTaskUserDetail> findWeLiveTaskCustomerDetail(Integer sendTargetType,String userName,String liveId,Integer sendState);


    /**
     *  分享统计（客户或客群）-tab
     * @param liveId
     * @param sendTargetType 1:客户 2:客群
     * @return
     */
    WeLiveTaskDetailTab findWeLiveTaskExecuteUserDetailTab(String liveId, Integer sendTargetType);


    /**
     *  分享统计（员工）-tab
     * @param liveId
     * @param sendTargetType
     * @return
     */
    WeLiveTaskDetailTab findWeLiveTaskUserDetailTab(String liveId,Integer sendTargetType);


}
