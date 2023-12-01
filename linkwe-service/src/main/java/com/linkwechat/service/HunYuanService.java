package com.linkwechat.service;

import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.config.HunYuanClient;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.hunyuan.v20230901.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author danmo
 * @date 2023-11-30 13:41
 **/
@Service
public class HunYuanService {
    @Autowired
    private LinkWeChatConfig linkWeChatConfig;

    public void sendMsg(Message [] msgList, Consumer<String> consumer) {
        HunYuanClient client = new HunYuanClient(new Credential(linkWeChatConfig.getTxAiSecretId(), linkWeChatConfig.getTxAiSecretKey()), linkWeChatConfig.getTxAiRegion());
        client.sendMsg(msgList, consumer);
    }
}
