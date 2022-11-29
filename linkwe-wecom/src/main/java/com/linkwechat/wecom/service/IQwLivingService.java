package com.linkwechat.wecom.service;

import com.linkwechat.domain.wecom.query.living.*;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.living.*;

/**
 * @author danmo 直播
 * @date 2022年10月12日 11:28
 */
public interface IQwLivingService {
    /**
     * 创建预约直播
     * @param query
     * @return
     */
    WeAddLivingVo create(WeAddLivingQuery query);

    /**
     * 修改预约直播
     * @param query
     * @return
     */
    WeResultVo modifyLiving(WeModifyLivingQuery query);

    /**
     * 取消预约直播
     * @param query
     * @return
     */
    WeResultVo cancelLiving(WeLivingQuery query);

    /**
     * 删除直播回放
     * @param query
     * @return
     */
    WeResultVo deleteReplayData(WeLivingQuery query);

    /**
     * 在微信中观看直播或直播回放
     * @param query
     * @return
     */
    WeGetLivingCodeVo getLivingCode(WeGetLivingCodeQuery query);

    /**
     * 获取成员直播ID列表
     * @param query
     * @return
     */
    WeLivingIdListVo getUserAllLivingId(WeGetUserAllLivingIdQuery query);


    /**
     * 获取直播详情
     * @param query
     * @return
     */
    WeLivingInfoVo getLivingInfo(WeLivingQuery query);

    /**
     * 获取直播观看明细
     * @param query
     * @return
     */
    WeLivingStatInfoVo getWatchStat(WeLivingQuery query);

    /**
     * 获取跳转小程序商城的直播观众信息
     * @param query
     * @return
     */
    WeGetLivingShareInfoVo getLivingShareInfo(WeGetLivingShareInfoQuery query);
}
