package com.linkwechat.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeKfMsg;
import com.linkwechat.domain.kf.query.WeKfRecordQuery;
import com.linkwechat.domain.kf.vo.WeKfRecordVo;
import com.linkwechat.domain.wecom.vo.kf.WeKfStateVo;

import java.util.Date;
import java.util.List;

/**
 * 客服消息表(WeKfMsg)
 *
 * @author danmo
 * @since 2022-04-15 15:53:36
 */
public interface IWeKfMsgService extends IService<WeKfMsg> {
    /**
     * 保存客服会话
     * @param msgList
     */
    void saveMsgOrEvent(List<JSONObject> msgList);

    /**
     * 获取客户最后会话消息
     * @return
     */
    WeKfMsg getLastCustomerMsg(String openKfId, String externalUserId, Date startTime);


    /**
     * 获取会话状态
     *
     * @param corpId
     * @param openKfId 客服ID
     * @param externalUserId 客户id
     * @return
     */
    WeKfStateVo getKfServiceState(String corpId, String openKfId, String externalUserId);

    /**
     * 会话详情
     * @param query
     * @return
     */
    List<WeKfRecordVo> getRecordDetail(WeKfRecordQuery query);

    WeKfMsg getLastCustomerMsg(String corpId, String openKfId, String externalUserId, String sendTime);
} 
