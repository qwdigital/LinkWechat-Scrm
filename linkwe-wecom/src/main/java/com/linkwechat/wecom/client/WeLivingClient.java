package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.domain.wecom.query.living.*;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.living.*;
import com.linkwechat.wecom.interceptor.WeLiveAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * 直播管理
 *
 * @author danmo
 * @date 2022年10月11日 16:09
 */
@BaseRequest(baseURL = "${weComServerUrl}", interceptor = WeLiveAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeLivingClient {

    /**
     * 创建预约直播
     *
     * @param query in {@link WeAddLivingQuery}
     * @return {@link WeAddLivingVo}
     */
    @Post(url = "living/create")
    WeAddLivingVo createLiving(@JSONBody WeAddLivingQuery query);

    /**
     * 修改预约直播
     *
     * @param query in {@link WeModifyLivingQuery}
     * @return {@link WeResultVo}
     */
    @Post(url = "living/modify")
    WeResultVo modifyLiving(@JSONBody WeModifyLivingQuery query);

    /**
     * 取消预约直播
     *
     * @param query in {@link WeLivingQuery}
     * @return {@link WeResultVo}
     */
    @Post(url = "living/cancel")
    WeResultVo cancelLiving(@JSONBody WeLivingQuery query);

    /**
     * 删除直播回放
     *
     * @param query in {@link WeLivingQuery}
     * @return {@link WeResultVo}
     */
    @Post(url = "living/delete_replay_data")
    WeResultVo deleteReplayData(@JSONBody WeLivingQuery query);

    /**
     * 在微信中观看直播或直播回放
     *
     * @param query in {@link WeGetLivingCodeQuery}
     * @return {@link WeGetLivingCodeVo}
     */
    @Post(url = "living/get_living_code")
    WeGetLivingCodeVo getLivingCode(@JSONBody WeGetLivingCodeQuery query);


    /**
     * 获取成员直播ID列表
     *
     * @param query in {@link WeGetUserAllLivingIdQuery}
     * @return {@link WeLivingIdListVo}
     */
    @Post(url = "living/get_user_all_livingid")
    WeLivingIdListVo getUserAllLivingId(@JSONBody WeGetUserAllLivingIdQuery query);

    /**
     * 获取直播详情
     *
     * @param query in {@link WeLivingQuery}
     * @return {@link WeLivingInfoVo}
     */
    @Post(url = "living/get_living_info?livingid=${query.livingid}")
    WeLivingInfoVo getLivingInfo(@Var("query") WeLivingQuery query);

    /**
     * 获取直播观看明细
     *
     * @param query in {@link WeLivingQuery}
     * @return {@link WeLivingStatInfoVo}
     */
    @Post(url = "living/get_watch_stat")
    WeLivingStatInfoVo getWatchStat(@JSONBody WeLivingQuery query);

    /**
     * 获取跳转小程序商城的直播观众信息
     *
     * @param query in {@link WeGetLivingShareInfoQuery}
     * @return {@link WeGetLivingShareInfoVo}
     */
    @Post(url = "living/get_living_share_info")
    WeGetLivingShareInfoVo getLivingShareInfo(@JSONBody WeGetLivingShareInfoQuery query);
}
