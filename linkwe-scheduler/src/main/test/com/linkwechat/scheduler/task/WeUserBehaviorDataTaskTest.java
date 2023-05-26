package com.linkwechat.scheduler.task;

import com.linkwechat.scheduler.LinkWechatSchedulerApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest(classes = LinkWechatSchedulerApplication.class)
@RunWith(SpringRunner.class)
class WeUserBehaviorDataTaskTest {

    @Autowired
    private WeUserBehaviorDataTask weUserBehaviorDataTask;

    @Autowired
    private WeGroupChatStatisticTask weGroupChatStatisticTask;

    @Test
    public void process() throws InterruptedException {
        //weUserBehaviorDataTask.process("{\"beginTime\":-30,\"endTime\":-1}");
        //weGroupChatStatisticTask.process("{\"beginTime\":-30,\"endTime\":-1}");
        //Thread.sleep(10000000);
    }
}