package com.linkwechat.factory.impl.kf;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.enums.WeKfStatusEnum;
import com.linkwechat.domain.WeKfInfo;
import com.linkwechat.domain.WeKfPool;
import com.linkwechat.domain.wecom.vo.kf.WeKfSyncEventMsgVo;
import com.linkwechat.factory.WeKfEventStrategy;
import com.linkwechat.fegin.QwKfClient;
import com.linkwechat.service.IWeKfInfoService;
import com.linkwechat.service.IWeKfPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

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

    @Resource
    private QwKfClient qwKfClient;

    @Override
    public void eventHandle(JSONObject message) {
        String msgStr = message.toJSONString();
        log.info("客服会话状态变更: msg:{}", msgStr);
        WeKfSyncEventMsgVo weKfSyncEventMs = JSONObject.parseObject(msgStr, WeKfSyncEventMsgVo.class);
        SecurityContextHolder.setCorpId(weKfSyncEventMs.getCorpId());
        //从接待池接入会话
        if(ObjectUtil.equal(1,weKfSyncEventMs.getChangeType())){
            String msgCode = weKfSyncEventMs.getMsgCode();
            WeKfPool weKfPool = new WeKfPool();
            weKfPool.setOpenKfId(weKfSyncEventMs.getOpenKfId());
            weKfPool.setExternalUserId(weKfSyncEventMs.getExternalUserId());
            weKfPool.setUserId(weKfSyncEventMs.getNewServicerUserId());
            weKfPool.setMsgCode(msgCode);
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
            WeKfInfo weKfInfo = weKfInfoService.getKfDetailByOpenKfId(weKfSyncEventMs.getCorpId(), weKfSyncEventMs.getOpenKfId());
            if(weKfInfo == null){
                return;
            }
            WeKfPool weKfPool = weKfPoolService.getKfPoolInfo(weKfSyncEventMs.getCorpId(), weKfSyncEventMs.getOpenKfId(), weKfSyncEventMs.getExternalUserId());
            weKfPool.setStatus(WeKfStatusEnum.STAR_OR_END.getType());
            weKfPool.setDelFlag(1);
            weKfPool.setSessionEndTime(new Date());
            weKfPoolService.updateKfPoolInfo(weKfPool);

            //发送结束语
            weKfInfoService.sendEndMsg(weKfSyncEventMs.getMsgCode(),weKfInfo,weKfPool);

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
