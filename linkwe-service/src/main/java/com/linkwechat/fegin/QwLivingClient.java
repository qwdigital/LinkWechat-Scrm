package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.living.*;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.living.*;
import com.linkwechat.fallback.QwLivingFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author danmo
 * @description 直播接口
 * @date 2022/10/11 20:54
 **/
@FeignClient(value = "${wecom.serve.linkwe-wecom}", fallback = QwLivingFallbackFactory.class)
public interface QwLivingClient {

    /**
     * 创建预约直播
     *
     * @param query corpid
     * @return
     */
    @PostMapping("/living/create")
    public AjaxResult<WeAddLivingVo> create(@RequestBody WeAddLivingQuery query);

    /**
     * 修改预约直播
     * @param query
     * @return
     */
    @PostMapping("/living/modif")
    public AjaxResult<WeResultVo> modifyLiving(@RequestBody WeModifyLivingQuery query);

    /**
     * 取消预约直播
     * @param query
     * @return
     */
    @PostMapping("/living/cancel")
    public AjaxResult<WeResultVo> cancelLiving(@RequestBody WeLivingQuery query);

    /**
     * 删除直播回放
     * @param query
     * @return
     */
    @PostMapping("/living/deleteReplayData")
    public AjaxResult<WeResultVo> deleteReplayData(@RequestBody WeLivingQuery query);

    /**
     * 在微信中观看直播或直播回放
     * @param query
     * @return
     */
    @PostMapping("/living/getLivingCode")
    public AjaxResult<WeGetLivingCodeVo> getLivingCode(@RequestBody WeGetLivingCodeQuery query);

    /**
     * 获取成员直播ID列表
     * @param query
     * @return
     */
    @PostMapping("/living/getUserAllLivingId")
    public AjaxResult<WeLivingIdListVo> getUserAllLivingId(@RequestBody WeGetUserAllLivingIdQuery query) ;


    /**
     * 获取直播明细
     * @param query
     * @return
     */
    @PostMapping("/living/getLivingInfo")
    public AjaxResult<WeLivingInfoVo> getLivingInfo(@RequestBody WeLivingQuery query);

    /**
     * 获取直播观看明细
     * @param query
     * @return
     */
    @PostMapping("/living/getWatchStat")
    public AjaxResult<WeLivingStatInfoVo> getWatchStat(@RequestBody WeLivingQuery query);

    /**
     * 获取跳转小程序商城的直播观众信息
     * @param query
     * @return
     */
    @PostMapping("/living/getLivingShareInfo")
    public AjaxResult<WeGetLivingShareInfoVo> getLivingShareInfo(@RequestBody WeGetLivingShareInfoQuery query);
}
