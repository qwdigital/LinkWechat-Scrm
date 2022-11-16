package com.linkwechat.factory.impl.kf;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.enums.WeKfMsgFailTypeEnum;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeKfCustomer;
import com.linkwechat.domain.WeKfPool;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.msg.QwAppMsgBody;
import com.linkwechat.domain.wecom.vo.kf.WeKfSyncEventMsgVo;
import com.linkwechat.factory.WeKfEventStrategy;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.service.IWeKfCustomerService;
import com.linkwechat.service.IWeKfPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author danmo
 * @description 客服消息发送失败
 * @date 2022/1/20 22:10
 **/
@Slf4j
@Service("msg_send_fail")
public class WeKfEventMsgSendFailImpl extends WeKfEventStrategy {

    @Autowired
    private RedisService redisService;

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Autowired
    private IWeKfCustomerService weKfCustomerService;

    @Autowired
    private IWeKfPoolService weKfPoolService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;


    @Override
    public void eventHandle(JSONObject message) {
        String msgStr = message.toJSONString();
        log.info("客服会话消息发送失败: msg:{}", msgStr);
        WeKfSyncEventMsgVo weKfSyncEventMs = JSONObject.parseObject(msgStr, WeKfSyncEventMsgVo.class);

        //WeKfPool kfPoolInfo = weKfPoolService.getKfPoolInfo(weKfSyncEventMs.getCorpId(), weKfSyncEventMs.getOpenKfId(), weKfSyncEventMs.getExternalUserId());
        WeKfPool kfPoolInfo = weKfPoolService.getOne(new LambdaQueryWrapper<WeKfPool>()
                .eq(WeKfPool::getCorpId,weKfSyncEventMs.getCorpId())
                .eq(WeKfPool::getOpenKfId,weKfSyncEventMs.getOpenKfId())
                .eq(WeKfPool::getExternalUserId,weKfSyncEventMs.getExternalUserId())
                .orderByDesc(BaseEntity::getUpdateTime).last("limit 1"));

        WeKfCustomer customerInfo = weKfCustomerService.getCustomerInfo(weKfSyncEventMs.getCorpId(), weKfSyncEventMs.getExternalUserId());
        String msg = WeKfMsgFailTypeEnum.parseEnum(weKfSyncEventMs.getFailType()).getMsg();
        String textStr = StringUtils.format("您与客户 {} 的客服消息发送失败。原因：{}", customerInfo.getNickName(), msg);
        WeMessageTemplate template = new WeMessageTemplate();
        template.setMsgType(MessageType.TEXT.getMessageType());
        template.setContent(textStr);
        QwAppMsgBody qwAppMsgBody = new QwAppMsgBody();
        qwAppMsgBody.setCorpId(weKfSyncEventMs.getCorpId());
        qwAppMsgBody.setCorpUserIds(ListUtil.toList(kfPoolInfo.getUserId()));
        qwAppMsgBody.setMessageTemplates(template);
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeAppMsgEx(), rabbitMQSettingConfig.getWeAppMsgQu(), JSONObject.toJSONString(qwAppMsgBody));
    }

}
