package com.linkwechat.wecom.strategy;

import com.linkwechat.wecom.domain.WeMessagePush;

public interface Strategy {

    /**
     * 推送消息
     *
     * @param weMessagePush 消息发送的
     */
    public void sendMessage(WeMessagePush weMessagePush);


}
