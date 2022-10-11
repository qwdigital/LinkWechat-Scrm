package com.linkwechat.factory.impl.kf;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeKfPool;
import com.linkwechat.domain.wecom.vo.kf.WeKfStateVo;
import com.linkwechat.domain.wecom.vo.kf.WeKfSyncEventMsgVo;
import com.linkwechat.factory.WeKfEventStrategy;
import com.linkwechat.service.IWeKfCustomerService;
import com.linkwechat.service.IWeKfMsgService;
import com.linkwechat.service.IWeKfPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author danmo
 * @description 客服进入会话
 * @date 2022/1/20 22:10
 **/
@Slf4j
@Service("enter_session")
public class WeKfEventEnterSessionImpl extends WeKfEventStrategy {

    @Autowired
    private IWeKfMsgService weKfMsgService;

    @Autowired
    private IWeKfCustomerService weKfCustomerService;

    @Autowired
    private IWeKfPoolService weKfPoolService;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void eventHandle(JSONObject message) {
        String msgStr = message.toJSONString();
        log.info("客服消息进入会话: msg:{}", msgStr);
        WeKfSyncEventMsgVo weKfSyncEventMs = JSONObject.parseObject(msgStr, WeKfSyncEventMsgVo.class);
        //客户信息存储
        weKfCustomerService.saveCustomerInfo(weKfSyncEventMs.getCorpId(),weKfSyncEventMs.getExternalUserId());


        WeKfStateVo kfState = weKfMsgService.getKfServiceState(weKfSyncEventMs.getCorpId(),weKfSyncEventMs.getOpenKfId(), weKfSyncEventMs.getExternalUserId());

        WeKfPool kfPoolInfo = weKfPoolService.getKfPoolInfo(weKfSyncEventMs.getCorpId(),weKfSyncEventMs.getOpenKfId(), weKfSyncEventMs.getExternalUserId());
        if(kfPoolInfo == null){
            WeKfPool weKfPool = new WeKfPool();
            weKfPool.setCorpId(weKfSyncEventMs.getCorpId());
            weKfPool.setOpenKfId(weKfSyncEventMs.getOpenKfId());
            weKfPool.setExternalUserId(weKfSyncEventMs.getExternalUserId());
            weKfPool.setScene(weKfSyncEventMs.getScene());
            weKfPool.setEnterTime(weKfSyncEventMs.getSendTime());
            weKfPool.setStatus(kfState.getServiceState());
            weKfPool.setUserId(kfState.getServicerUserid());
            weKfPoolService.saveKfPoolInfo(weKfPool);
        }
        //发送欢迎语
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeWelcomeMsgEx(),rabbitMQSettingConfig.getWeKfWelcomeMsgRk(),msgStr);
    }

}