package com.linkwechat.factory.impl;

import com.linkwechat.LinkWeEventTaskApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = LinkWeEventTaskApplication.class)
class WeMsgAuditNotifyImplTest {

    @Autowired
    private WeMsgAuditNotifyImpl weMsgAuditNotify;

    @Test
    void eventHandle() {
        String msg = "";
        weMsgAuditNotify.eventHandle(msg);
    }
}