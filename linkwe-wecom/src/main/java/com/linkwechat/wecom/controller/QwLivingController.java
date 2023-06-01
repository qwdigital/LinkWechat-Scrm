package com.linkwechat.wecom.controller;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.living.*;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.living.*;
import com.linkwechat.wecom.service.IQwLivingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author danmo
 * @description 企微企业接口
 * @date 2022/3/13 21:01
 **/
@Slf4j
@RestController
@RequestMapping("living")
public class QwLivingController {

    @Autowired
    private IQwLivingService qwLivingService;

    /**
     * 创建预约直播
     *
     * @param query corpid
     * @return
     */
    @PostMapping("/create")
    public AjaxResult<WeAddLivingVo> create(@RequestBody WeAddLivingQuery query) {
        WeAddLivingVo weAddLiving = qwLivingService.create(query);
        return AjaxResult.success(weAddLiving);
    }

    /**
     * 修改预约直播
     * @param query
     * @return
     */
    @PostMapping("/modif")
    public AjaxResult<WeResultVo> modifyLiving(@RequestBody WeModifyLivingQuery query) {
        WeResultVo weResult = qwLivingService.modifyLiving(query);
        return AjaxResult.success(weResult);
    }

    /**
     * 取消预约直播
     * @param query
     * @return
     */
    @PostMapping("/cancel")
    public AjaxResult<WeResultVo> cancelLiving(@RequestBody WeLivingQuery query) {
        WeResultVo weResult = qwLivingService.cancelLiving(query);
        return AjaxResult.success(weResult);
    }

    /**
     * 删除直播回放
     * @param query
     * @return
     */
    @PostMapping("/deleteReplayData")
    public AjaxResult<WeResultVo> deleteReplayData(@RequestBody WeLivingQuery query) {
        WeResultVo weResult = qwLivingService.deleteReplayData(query);
        return AjaxResult.success(weResult);
    }

    /**
     * 在微信中观看直播或直播回放
     * @param query
     * @return
     */
    @PostMapping("/getLivingCode")
    public AjaxResult<WeGetLivingCodeVo> getLivingCode(@RequestBody WeGetLivingCodeQuery query) {
        WeGetLivingCodeVo weGetLivingCode = qwLivingService.getLivingCode(query);
        return AjaxResult.success(weGetLivingCode);
    }

    /**
     * 获取成员直播ID列表
     * @param query
     * @return
     */
    @PostMapping("/getUserAllLivingId")
    public AjaxResult<WeLivingIdListVo> getUserAllLivingId(@RequestBody WeGetUserAllLivingIdQuery query) {
        WeLivingIdListVo weLivingIdList = qwLivingService.getUserAllLivingId(query);
        return AjaxResult.success(weLivingIdList);
    }


    /**
     * 获取直播详情
     * @param query
     * @return
     */
    @PostMapping("/getLivingInfo")
    public AjaxResult<WeLivingInfoVo> getLivingInfo(@RequestBody WeLivingQuery query){

        return AjaxResult.success(
                qwLivingService.getLivingInfo(query)
        );
    }

    /**
     * 获取直播观看明细
     * @param query
     * @return
     */
    @PostMapping("/getWatchStat")
    public AjaxResult<WeLivingStatInfoVo> getWatchStat(@RequestBody WeLivingQuery query) {
        WeLivingStatInfoVo weLivingStatInfo = qwLivingService.getWatchStat(query);
        return AjaxResult.success(weLivingStatInfo);
    }

    /**
     * 获取跳转小程序商城的直播观众信息
     * @param query
     * @return
     */
    @PostMapping("/getLivingShareInfo")
    public AjaxResult<WeGetLivingShareInfoVo> getLivingShareInfo(@RequestBody WeGetLivingShareInfoQuery query) {
        WeGetLivingShareInfoVo weGetLivingShareInfo = qwLivingService.getLivingShareInfo(query);
        return AjaxResult.success(weGetLivingShareInfo);
    }
}
