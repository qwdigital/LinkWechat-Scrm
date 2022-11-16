package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.WeKfMsg;
import com.linkwechat.domain.kf.query.WeKfRecordQuery;
import com.linkwechat.domain.kf.vo.WeKfRecordVo;
import com.linkwechat.domain.wecom.query.kf.WeKfStateQuery;
import com.linkwechat.domain.wecom.vo.kf.WeKfStateVo;
import com.linkwechat.domain.wecom.vo.kf.WeKfSyncMsgVo;
import com.linkwechat.domain.wecom.vo.media.WeMediaVo;
import com.linkwechat.fegin.QwKfClient;
import com.linkwechat.mapper.WeKfMsgMapper;
import com.linkwechat.service.IWeKfMsgService;
import com.linkwechat.service.IWeMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 客服消息表(WeKfMsg)
 *
 * @author danmo
 * @since 2022-04-15 15:53:36
 */
@Slf4j
@Service
public class WeKfMsgServiceImpl extends ServiceImpl<WeKfMsgMapper, WeKfMsg> implements IWeKfMsgService {

    @Autowired
    private QwKfClient qwKfClient;

    @Autowired
    private IWeMaterialService weMaterialService;

    @Override
    public void saveMsgOrEvent(List<JSONObject> msgList) {
        if (CollectionUtil.isEmpty(msgList)) {
            return;
        }
        List<WeKfMsg> weKfMsgList = msgList.stream().map(msgInfo -> {
            WeKfSyncMsgVo weKfSyncMsg = JSONObject.parseObject(msgInfo.toJSONString(), WeKfSyncMsgVo.class);
            JSONObject msgInfoVo = msgInfo.getJSONObject(weKfSyncMsg.getMsgType());
            try {
                if(msgInfoVo.get("media_id") != null || msgInfoVo.get("thumb_media_id") != null){
                    String mediaId = "";
                    if(msgInfoVo.get("media_id") != null){
                        mediaId = msgInfoVo.getString("media_id");
                    }else if(msgInfoVo.get("thumb_media_id") != null){
                        mediaId = msgInfoVo.getString("thumb_media_id");
                    }

                    WeMediaVo weMediaVo = weMaterialService.getMediaToResponse(mediaId);
                    msgInfoVo.put("attachment",weMediaVo.getUrl());
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("下载素材失败 msgId:{}",weKfSyncMsg.getMsgId());
            }
            WeKfMsg weKfMsg = new WeKfMsg();
            weKfMsg.setCorpId(SecurityUtils.getCorpId());
            weKfMsg.setOpenKfId(weKfSyncMsg.getOpenKfId());
            weKfMsg.setExternalUserid(weKfSyncMsg.getExternalUserId());
            weKfMsg.setMsgId(weKfSyncMsg.getMsgId());
            weKfMsg.setMsgType(weKfSyncMsg.getMsgType());
            weKfMsg.setServicerUserid(weKfSyncMsg.getServicerUserId());
            weKfMsg.setOrigin(weKfSyncMsg.getOrigin());
            weKfMsg.setSendTime(new Date(weKfSyncMsg.getSendTime() * 1000));
            weKfMsg.setContent(msgInfoVo.toJSONString());
            return weKfMsg;
        }).collect(Collectors.toList());
        saveBatch(weKfMsgList);
    }

    @Override
    public WeKfMsg getLastCustomerMsg(String openKfId, String externalUserId, Date startTime) {
        return getOne(new LambdaQueryWrapper<WeKfMsg>()
                .eq(WeKfMsg::getOpenKfId, openKfId)
                .eq(WeKfMsg::getExternalUserid, externalUserId)
                .orderByDesc(WeKfMsg::getSendTime)
                .last("limit 1"));
    }


    @Override
    public WeKfStateVo getKfServiceState(String corpId, String openKfId, String externalUserId) {
        WeKfStateQuery stateQuery = new WeKfStateQuery();
        stateQuery.setOpen_kfid(openKfId);
        stateQuery.setCorpid(corpId);
        stateQuery.setExternal_userid(externalUserId);
        return qwKfClient.getSessionStatus(stateQuery).getData();
    }

    @Override
    public List<WeKfRecordVo> getRecordDetail(WeKfRecordQuery query) {
        return this.baseMapper.getRecordDetail(query);
    }

    @Override
    public WeKfMsg getLastCustomerMsg(String corpId, String openKfId, String externalUserId, String sendTime) {
        return getOne(new LambdaQueryWrapper<WeKfMsg>()
                .eq(WeKfMsg::getCorpId,corpId)
                .eq(WeKfMsg::getOpenKfId, openKfId)
                .eq(WeKfMsg::getExternalUserid, externalUserId)
                .ge(WeKfMsg::getSendTime, sendTime)
                .orderByDesc(WeKfMsg::getSendTime)
                .last("limit 1"));
    }

}
