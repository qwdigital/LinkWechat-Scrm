package com.linkwechat.quartz.task;

import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.wecom.domain.WeCustomerMessageTimeTask;
import com.linkwechat.wecom.mapper.WeCustomerMessageTimeTaskMapper;
import com.linkwechat.wecom.service.IWeCustomerMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @author danmo
 * @description 消息群发任务
 * @date 2021/7/15 22:59
 **/
@Slf4j
@Component("groupMessageTask")
public class GroupMessageTask {
    @Autowired
    private WeCustomerMessageTimeTaskMapper customerMessageTimeTaskMapper;

    @Autowired
    private IWeCustomerMessageService weCustomerMessageService;

    /**
     * 扫描群发消息定时任务(只是处理当天的群发消息)
     */
    public void messageTask() {
        //获的当前时间的毫秒数
        //customerMessageTimeTaskMapper
        List<WeCustomerMessageTimeTask> weCustomerMessageTimeTasks = customerMessageTimeTaskMapper.selectWeCustomerMessageTimeTaskGteSettingTime(System.currentTimeMillis());
        if (CollectionUtils.isNotEmpty(weCustomerMessageTimeTasks)) {
            weCustomerMessageTimeTasks.forEach(
                    s -> {
                        Integer solved=new Integer(1);
                        String exceMsg=null;
                        try {
                            if (s.getMessageInfo() != null && s.getMessageId() != null || (s.getMessageInfo().getPushType().equals(WeConstans.SEND_MESSAGE_CUSTOMER)
                                    && CollectionUtils.isNotEmpty(s.getCustomersInfo())) || (s.getMessageInfo().getPushType().equals(WeConstans.SEND_MESSAGE_GROUP)
                                    && CollectionUtils.isNotEmpty(s.getGroupsInfo()))) {
                                weCustomerMessageService.sendMessgae(s.getMessageInfo(), s.getMessageId(), s.getCustomersInfo(), s.getGroupsInfo());

                            }
                        } catch (Exception e) {
                            log.error("定时群发消息处理异常：ex:{}", e);
                            solved=new Integer(2);
                            exceMsg=e.getMessage();
                        }finally {
                            //更新消息处理状态
                            customerMessageTimeTaskMapper.updateTaskSolvedById(s.getTaskId(),solved,exceMsg);
                        }
                    }
            );
        }

    }
}
