package com.linkwechat.scheduler.listener;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.scheduler.LinkWechatSchedulerApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = LinkWechatSchedulerApplication.class)
class QwChatMsgCheckListenerTest {

    @Autowired
    private QwWelcomeMsgListener qwWelcomeMsgListener;

    @Test
    void subscribe() throws IOException {
        String msg = "{\"changeType\":\"add_external_contact\",\"createTime\":1678412858,\"event\":\"change_external_contact\",\"externalUserID\":\"wmbhUTLgAA2PSgZCGp0un2b-8bP2yO3g\",\"fromUserName\":\"sys\",\"msgType\":\"event\",\"state\":\"1592099060930727936\",\"toUserName\":\"ww622fc852f79c3f13\",\"userID\":\"45DuXiangShangQingXie\",\"welcomeCode\":\"205ttN9RtSSig5viZIQ5Pz7NiHqzh0tnXoiXFHuMUWk\"}";
        qwWelcomeMsgListener.subscribe(msg,null,null);
    }
}