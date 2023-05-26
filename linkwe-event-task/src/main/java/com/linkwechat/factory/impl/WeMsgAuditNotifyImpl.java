package com.linkwechat.factory.impl;

import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeChatContactMsg;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.factory.WeCallBackEventFactory;
import com.linkwechat.service.IWeChatContactMsgService;
import com.linkwechat.service.IWeCorpAccountService;
import com.tencent.wework.FinanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author danmo
 * @description 产生会话回调事件
 * @date 2021/1/20 1:13
 **/
@Service
@Slf4j
public class WeMsgAuditNotifyImpl implements WeCallBackEventFactory {

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Autowired
    private IWeChatContactMsgService weChatContactMsgService;

    @Autowired
    private RedisService redisService;

    @Override
    public void eventHandle(String message) {
        WeBackBaseVo weBackBaseVo = XmlUtil.xmlToBean(XmlUtil.parseXml(message).getFirstChild(), WeBackBaseVo.class);

        String corpId = weBackBaseVo.getToUserName();
        log.info("会话拉取定时任务--------------{}",corpId);
        Long seqLong = 0L;

        WeCorpAccount corpAccount = weCorpAccountService.getCorpAccountByCorpId(corpId);
        if (corpAccount == null) {
            log.info("无有效企业----------------->");
            return;
        }

        if(redisService.keyIsExists("we:chat:seq:" + corpAccount.getCorpId())){
            seqLong = (Long) redisService.getCacheObject("we:chat:seq:" + corpAccount.getCorpId());
        }else {
            LambdaQueryWrapper<WeChatContactMsg> wrapper = new LambdaQueryWrapper<WeChatContactMsg>().orderByDesc(WeChatContactMsg::getSeq).last("limit 1");
            WeChatContactMsg weChatContactMsg = weChatContactMsgService.getOne(wrapper);
            if (weChatContactMsg != null) {
                seqLong = weChatContactMsg.getSeq();
            }
        }
        FinanceService financeService = new FinanceService(corpAccount.getCorpId(), corpAccount.getChatSecret(), corpAccount.getFinancePrivateKey());
        financeService.setRedisService(redisService);
        financeService.getChatData(seqLong, (data) -> rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeChatMsgAuditEx(), rabbitMQSettingConfig.getWeChatMsgAuditRk(), data.toJSONString()));
        log.info("会话存档定时任务执行完成----------------->");
    }

}
