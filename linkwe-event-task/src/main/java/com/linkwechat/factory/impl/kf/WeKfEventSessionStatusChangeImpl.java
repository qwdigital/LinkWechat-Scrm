package com.linkwechat.factory.impl.kf;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.enums.WeKfMsgTypeEnum;
import com.linkwechat.common.enums.WeKfStatusEnum;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeKfPool;
import com.linkwechat.domain.kf.vo.WeKfInfoVo;
import com.linkwechat.domain.wecom.query.kf.WeKfMsgQuery;
import com.linkwechat.domain.wecom.vo.kf.WeKfSyncEventMsgVo;
import com.linkwechat.factory.WeKfEventStrategy;
import com.linkwechat.fegin.QwKfClient;
import com.linkwechat.service.IWeKfInfoService;
import com.linkwechat.service.IWeKfPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author danmo
 * @description 会话状态变更事件
 * @date 2022/1/20 22:10
 **/
@Slf4j
@Service("session_status_change")
public class WeKfEventSessionStatusChangeImpl extends WeKfEventStrategy {
    @Autowired
    private RedisService redisService;

    @Autowired
    private IWeKfPoolService weKfPoolService;

    @Autowired
    private IWeKfInfoService weKfInfoService;

    @Autowired
    private QwKfClient qwKfClient;

    @Override
    public void eventHandle(JSONObject message) {
        String msgStr = message.toJSONString();
        log.info("客服会话状态变更: msg:{}", msgStr);
        WeKfSyncEventMsgVo weKfSyncEventMs = JSONObject.parseObject(msgStr, WeKfSyncEventMsgVo.class);
        //从接待池接入会话
        if(ObjectUtil.equal(1,weKfSyncEventMs.getChangeType())){
            String msgCode = weKfSyncEventMs.getMsgCode();
            redisService.setCacheObject(StringUtils.format(WeConstans.KF_SESSION_MSG_CODE_KEY,weKfSyncEventMs.getCorpId(), weKfSyncEventMs.getOpenKfId(),weKfSyncEventMs.getNewServicerUserId()), msgCode,48, TimeUnit.HOURS);
            WeKfPool weKfPool = new WeKfPool();
            weKfPool.setOpenKfId(weKfSyncEventMs.getOpenKfId());
            weKfPool.setExternalUserId(weKfSyncEventMs.getExternalUserId());
            weKfPool.setUserId(weKfSyncEventMs.getNewServicerUserId());
            weKfPool.setSessionStartTime(new Date());
            weKfPoolService.updateKfPoolInfo(weKfPool);
            return;
        }
        //2-转接会话
        if(ObjectUtil.equal(2,weKfSyncEventMs.getChangeType())){
            weKfPoolService.updateKfServicer(weKfSyncEventMs.getCorpId(),weKfSyncEventMs.getOpenKfId(),weKfSyncEventMs.getExternalUserId(),weKfSyncEventMs.getNewServicerUserId());
            return;
        }
        //结束会话
        if(ObjectUtil.equal(3,weKfSyncEventMs.getChangeType())){
            WeKfInfoVo weKfInfo = weKfInfoService.getKfInfoByOpenKfId(weKfSyncEventMs.getCorpId(), weKfSyncEventMs.getOpenKfId());
            if(weKfInfo == null){
                return;
            }
            //发送结束语
            String endCode = weKfSyncEventMs.getMsgCode();
            WeKfMsgQuery weKfMsgQuery = new WeKfMsgQuery();
            JSONObject msgBody = new JSONObject();
            msgBody.put("content", weKfInfo.getEndContent());
            weKfMsgQuery.setMsgtype(WeKfMsgTypeEnum.TEXT.getType());
            weKfMsgQuery.setCode(endCode);
            weKfMsgQuery.setContext(msgBody);
            qwKfClient.sendMsgOnEvent(weKfMsgQuery);

            WeKfPool weKfPool = new WeKfPool();
            weKfPool.setCorpId(weKfSyncEventMs.getCorpId());
            weKfPool.setOpenKfId(weKfSyncEventMs.getOpenKfId());
            weKfPool.setExternalUserId(weKfSyncEventMs.getExternalUserId());
            weKfPool.setStatus(WeKfStatusEnum.STAR_OR_END.getType());
            weKfPool.setDelFlag(1);
            weKfPool.setSessionEndTime(new Date());
            weKfPoolService.updateKfPoolInfo(weKfPool);
            weKfPoolService.transferReceptionPoolCustomer(weKfSyncEventMs.getCorpId(),weKfSyncEventMs.getOpenKfId(),weKfSyncEventMs.getExternalUserId());
            return;
        }
        //4-重新接入已结束/已转接会话
        if(ObjectUtil.equal(4,weKfSyncEventMs.getChangeType())){
            WeKfPool kfPoolInfo = weKfPoolService.getKfPoolInfo(weKfSyncEventMs.getCorpId(),weKfSyncEventMs.getOpenKfId(), weKfSyncEventMs.getExternalUserId());
            if(kfPoolInfo == null){
                WeKfPool weKfPool = new WeKfPool();
                weKfPool.setCorpId(weKfSyncEventMs.getCorpId());
                weKfPool.setOpenKfId(weKfSyncEventMs.getOpenKfId());
                weKfPool.setExternalUserId(weKfSyncEventMs.getExternalUserId());
                weKfPool.setScene(weKfSyncEventMs.getScene());
                weKfPool.setEnterTime(weKfSyncEventMs.getSendTime());
                weKfPool.setStatus(weKfSyncEventMs.getStatus());
                weKfPool.setUserId(weKfSyncEventMs.getNewServicerUserId());
                weKfPoolService.saveKfPoolInfo(weKfPool);
            }
            return;
        }
    }
}
