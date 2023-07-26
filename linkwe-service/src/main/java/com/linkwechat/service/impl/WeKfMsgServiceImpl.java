package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.enums.WeKfOriginEnum;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeKfInfo;
import com.linkwechat.domain.WeKfMsg;
import com.linkwechat.domain.WeKfNoticeLog;
import com.linkwechat.domain.WeKfPool;
import com.linkwechat.domain.kf.query.WeKfRecordQuery;
import com.linkwechat.domain.kf.vo.WeKfMsgAnalyzeVo;
import com.linkwechat.domain.kf.vo.WeKfRecordVo;
import com.linkwechat.domain.wecom.query.kf.WeKfStateQuery;
import com.linkwechat.domain.wecom.vo.kf.WeKfStateVo;
import com.linkwechat.domain.wecom.vo.kf.WeKfSyncMsgVo;
import com.linkwechat.domain.wecom.vo.media.WeMediaVo;
import com.linkwechat.fegin.QwKfClient;
import com.linkwechat.mapper.WeKfMsgMapper;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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

    @Resource
    private QwKfClient qwKfClient;

    @Autowired
    private IWeMaterialService weMaterialService;

    @Lazy
    @Autowired
    private IWeKfInfoService weKfInfoService;

    @Lazy
    @Autowired
    private IWeKfPoolService weKfPoolService;

    @Autowired
    private IWeKfNoticeLogService weKfNoticeLogService;

    @Override
    public void saveMsgOrEvent(List<JSONObject> msgList) {
        if (CollectionUtil.isEmpty(msgList)) {
            return;
        }
        List<WeKfMsg> weKfMsgList = msgList.stream().map(msgInfo -> {
            WeKfSyncMsgVo weKfSyncMsg = JSONObject.parseObject(msgInfo.toJSONString(), WeKfSyncMsgVo.class);
            JSONObject msgInfoVo = msgInfo.getJSONObject(weKfSyncMsg.getMsgType());
            try {
                if (msgInfoVo.get("media_id") != null || msgInfoVo.get("thumb_media_id") != null) {
                    String mediaId = "";
                    if (msgInfoVo.get("media_id") != null) {
                        mediaId = msgInfoVo.getString("media_id");
                    } else if (msgInfoVo.get("thumb_media_id") != null) {
                        mediaId = msgInfoVo.getString("thumb_media_id");
                    }

                    WeMediaVo weMediaVo = weMaterialService.getMediaToResponse(mediaId);
                    msgInfoVo.put("attachment", weMediaVo.getUrl());
                }
            } catch (Exception e) {
                log.error("下载素材失败 msgId:{}", weKfSyncMsg.getMsgId(), e);
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

        if (CollectionUtil.isNotEmpty(weKfMsgList)) {
            for (WeKfMsg weKfMsg : weKfMsgList) {
                try {
                    JSONObject msgInfoVo = JSONObject.parseObject(weKfMsg.getContent());
                    //评价消息，写入池中
                    if (Objects.nonNull(msgInfoVo.get("menu_id")) && msgInfoVo.getString("menu_id").startsWith("end_")) {
                        String menuId = msgInfoVo.getString("menu_id");
                        String[] menuIdArr = menuId.split("_");
                        WeKfPool kfPool = weKfPoolService.getById(Long.valueOf(menuIdArr[2]));
                        if(Objects.nonNull(kfPool) && StringUtils.isEmpty(kfPool.getEvaluationType())){
                            WeKfPool weKfPool = new WeKfPool();
                            weKfPool.setId(kfPool.getId());
                            weKfPool.setEvaluationType(menuIdArr[1]);
                            weKfPoolService.updateById(weKfPool);
                        }
                    }
                } catch (NumberFormatException e) {
                    log.error("评价消息处理异常 weKfMsg:{}", JSONObject.toJSONString(weKfMsg), e);
                }
            }
        }
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
                .eq(WeKfMsg::getCorpId, corpId)
                .eq(WeKfMsg::getOpenKfId, openKfId)
                .eq(WeKfMsg::getExternalUserid, externalUserId)
                //.ge(WeKfMsg::getSendTime, sendTime)
                .orderByDesc(WeKfMsg::getSendTime)
                .last("limit 1"));
    }


    @Override
    public WeKfMsgAnalyzeVo getMsgAnalyze(WeKfRecordQuery query) {
        WeKfInfo weKfInfo = weKfInfoService.getOne(new LambdaQueryWrapper<WeKfInfo>().eq(WeKfInfo::getCorpId,SecurityUtils.getCorpId()).eq(WeKfInfo::getOpenKfId,query.getOpenKfId()).last("limit 1"));
        WeKfPool weKfPool = weKfPoolService.getById(query.getPoolId());
        Integer timeOutNotice = weKfInfo.getTimeOutNotice();
        Integer timeOutType = weKfInfo.getTimeOutType();
        Integer timeOut = weKfInfo.getTimeOut();



        WeKfMsgAnalyzeVo analyzeVo = new WeKfMsgAnalyzeVo();

        if (Objects.nonNull(weKfPool)) {
            String evaluation = weKfPool.getEvaluation();
            if(StringUtils.isNotEmpty(evaluation)){
                JSONObject evaluationObj = JSONObject.parseObject(evaluation);
                analyzeVo.setContent(evaluationObj.getString("content"));
                analyzeVo.setEvaluationType(evaluationObj.getString("menu_id"));
            }
            if(StringUtils.isNotEmpty(weKfPool.getEvaluationType())){
                analyzeVo.setEvaluationType(weKfPool.getEvaluationType());
            }
        }
        List<WeKfMsg> weKfMsgList = list(new LambdaQueryWrapper<WeKfMsg>()
                .eq(WeKfMsg::getOpenKfId, query.getOpenKfId())
                .eq(WeKfMsg::getExternalUserid, query.getExternalUserId())
                .ge(WeKfMsg::getSendTime, DateUtil.formatDateTime(weKfPool.getEnterTime()))
                .le(StringUtils.isNotEmpty(query.getEndTime()),WeKfMsg::getSendTime, DateUtil.formatDateTime(weKfPool.getSessionEndTime()))
                .orderByAsc(WeKfMsg::getSendTime)
        );
        //计算回复聊天对
        if (CollectionUtil.isNotEmpty(weKfMsgList)) {
            int i = 0;
            int j = 1;
            int outTimeNoticeCnt = 0;
            int count = 0;
            long durationCnt = 0L;
            while (j < weKfMsgList.size()) {
                if (Objects.equals(WeKfOriginEnum.CUSTOMER_SEND.getType(), weKfMsgList.get(i).getOrigin())
                        && Objects.equals(WeKfOriginEnum.SERVICER_SEND.getType(), weKfMsgList.get(j).getOrigin())) {
                    long duration = 0;
                    if (ObjectUtil.equal(1, timeOutType)) {
                        duration = DateUtil.between(weKfMsgList.get(i).getSendTime(), weKfMsgList.get(j).getSendTime(), DateUnit.MINUTE);
                    } else {
                        duration = DateUtil.between(weKfMsgList.get(i).getSendTime(), weKfMsgList.get(j).getSendTime(), DateUnit.HOUR);
                    }
                    if (timeOut <= duration) {
                        outTimeNoticeCnt++;
                        long minute = DateUtil.between(weKfMsgList.get(i).getSendTime(), weKfMsgList.get(j).getSendTime(), DateUnit.SECOND);
                        durationCnt += minute;
                    }

                    count++;
                }
                i++;
                j++;
            }
            if (ObjectUtil.equal(1, timeOutNotice)) {
                float asr = (float) outTimeNoticeCnt / count;
                if (!Float.isNaN(asr)) {
                    NumberFormat fmt = NumberFormat.getPercentInstance();
                    analyzeVo.setOutTimeRate(fmt.format(asr));
                }
                analyzeVo.setOutTimeDuration(durationCnt);
            }
        }
        int noticeCount = weKfNoticeLogService.count(new LambdaQueryWrapper<WeKfNoticeLog>()
                .eq(WeKfNoticeLog::getCorpId, SecurityUtils.getCorpId())
                .eq(WeKfNoticeLog::getOpenKfId, query.getOpenKfId())
                .eq(WeKfNoticeLog::getUserId, query.getUserId())
                .eq(WeKfNoticeLog::getExternalUserId, query.getExternalUserId())
                .ge(BaseEntity::getCreateTime, query.getBeginTime())
                .le(StringUtils.isNotEmpty(query.getEndTime()), BaseEntity::getCreateTime, query.getEndTime()));
        analyzeVo.setOutTimeNoticeCnt(noticeCount);
        return analyzeVo;
    }
}
