package com.linkwechat.wecom.strategy;

import com.linkwechat.wecom.domain.WeMessagePush;

public class MessageContext {

    private Strategy strategy;

    public MessageContext(Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 推送消息
     *
     * @param weMessagePush 消息发送的
     */
    public void sendMessage(WeMessagePush weMessagePush) {
        strategy.sendMessage(weMessagePush);
    }

}
