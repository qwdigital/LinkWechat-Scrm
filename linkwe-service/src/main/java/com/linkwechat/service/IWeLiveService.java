package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.live.WeLive;

import java.text.ParseException;
import java.util.List;

/**
* @author robin
* @description 针对表【we_live(直播主表)】的数据库操作Service
* @createDate 2022-10-26 22:40:45
*/
public interface IWeLiveService extends IService<WeLive> {

    /**
     * 新增或编辑直播
     * @param weLive
     */
    void addOrUpdate(WeLive weLive) throws ParseException;

    /**
     *  直播列表查询
     * @return
     */
    List<WeLive> findLives(WeLive weLive);

    /**
     * 获取直播数据基础详情
     * @param liveId
     * @return
     */
    WeLive findLiveDetail(String liveId);


    /**
     * 移除直播
     * @param liveIds
     */
    void removeLives(List<Long> liveIds);


    /**
     *取消直播
     */
    void cancleLive(WeLive live);


    /**
     * 直播同步消息
     */
    void synchLive();

    /**
     * 直播同步业务
     */
    void synchLiveHandler(String msg);

    /**
     * 同步直播详细业务
     * @param weLives
     */
    void synchLiveData(List<WeLive> weLives);


    /**
     * 获取直播凭证
     * @param livingId 企微返回的直播id
     * @param openId 微信客户openid
     */
    String getLivingCode(String livingId,String openId);


    /**
     * 同步发送结果
     * @param id
     */
    void synchExecuteResult(String id);


}
