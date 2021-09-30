package com.linkwechat.quartz.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.entity.WeCorpAccount;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.WeChatContactMsg;
import com.linkwechat.wecom.service.IWeChatContactMsgService;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import com.tencent.wework.FinanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 会话存档定时任务
 * @date 2021/7/26 15:50
 **/

@Slf4j
@Component("conversationArchiveTask")
public class ConversationArchiveTask {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Autowired
    private IWeChatContactMsgService weChatContactMsgService;

    public void FinanceTask(){
        log.info("会话存档定时任务执行----------------->");
        Long seqLong = 0L;
        WeCorpAccount corpAccount = weCorpAccountService.findValidWeCorpAccount();
        if(corpAccount == null){
            log.info("无有效企业----------------->");
            return;
        }
        LambdaQueryWrapper<WeChatContactMsg> wrapper = new LambdaQueryWrapper<WeChatContactMsg>().orderByDesc(WeChatContactMsg::getSeq).last("limit 1");
        WeChatContactMsg weChatContactMsg = weChatContactMsgService.getOne(wrapper);
        if(weChatContactMsg != null){
            seqLong = weChatContactMsg.getSeq();
        }
        FinanceService financeService = new FinanceService(corpAccount.getCorpId(),corpAccount.getChatSecret(),corpAccount.getFinancePrivateKey());
        financeService.getChatData(seqLong,(data) -> redisCache.convertAndSend(WeConstans.CONVERSATION_MSG_CHANNEL,data));
        log.info("会话存档定时任务执行完成----------------->");
    }
}
