package com.linkwechat.scheduler.listener;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.scheduler.LinkWechatSchedulerApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = LinkWechatSchedulerApplication.class)
class QwChatMsgCheckListenerTest {

    @Autowired
    private QwChatMsgCheckListener qwChatMsgCheckListener;

    @Test
    void subscribe() {
        String msg = "{\"corpId\":\"ww622fc852f79c3f13\",\"tolist\":[\"wmbhUTLgAASJ1W6ealJ1AFhPFC7vHasw\"],\"msgtime\":1663810608924,\"msgid\":\"9772783422504563801_1663810613452_external\",\"action\":\"send\",\"from\":\"DanMo\",\"text\":{\"content\":\"红包\"},\"msgtype\":\"text\",\"roomid\":\"\",\"seq\":466}";
        qwChatMsgCheckListener.msgContextHandle(JSONObject.parseObject(msg));
    }
}