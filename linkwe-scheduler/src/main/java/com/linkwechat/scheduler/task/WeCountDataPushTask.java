package com.linkwechat.scheduler.task;



import com.linkwechat.service.IWeOperationCenterService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * 统计相关数据推送
 */
@Slf4j
@Component
public class WeCountDataPushTask {

    @Autowired
    IWeOperationCenterService iWeOperationCenterService;


    @XxlJob("weGroupAndCustomerCountDataTask")
    public void process(){
        String jobParam = XxlJobHelper.getJobParam();
        log.info("动态日报:昨日客户与客群相关统计>>>>>>>>>>>>>>>>>>>启动 params:{}",jobParam);
        iWeOperationCenterService.pushData();
    }




}
