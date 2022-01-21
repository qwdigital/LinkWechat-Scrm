package com.linkwechat.wecom.service.event;

import com.linkwechat.wecom.domain.callback.WeCallBackEvent;
import com.linkwechat.wecom.domain.query.WeCustomerWelcomeQuery;
import com.linkwechat.wecom.domain.query.WeTaskEventQuery;
import com.linkwechat.wecom.domain.query.qr.WeQrCodeEventQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * @author danmo
 * @description 注册事件发布
 * @date 2021/11/13 19:01
 **/
@Service
@Slf4j
public class WeEventPublisherService implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }


    /**
     * 回调事件推送
     */
    public void register(String message) {
        log.info("回调事件推送：message:{}", message);
        applicationEventPublisher.publishEvent(new WeCallBackEvent(this, message));
    }

    /**
     * 发布欢迎语事件
     */
    public void register(String externalUserId, String userId, String welcomeCode, String state) {
        log.info("发布发送欢迎语事件：externalUserId:{},userId:{},welcomeCode:{},state:{}", externalUserId, userId, welcomeCode, state);
        applicationEventPublisher.publishEvent(new WeCustomerWelcomeQuery(this, externalUserId, userId, welcomeCode, state));
    }

    /**
     * 刷新活码
     */
    public void refreshQrCode(String qrId) {
        log.info("刷新活码任务：qrId:{}", qrId);
        applicationEventPublisher.publishEvent(new WeQrCodeEventQuery(this, qrId));
    }

    /**
     * 群发任务回调
     */
    public void callBackTask(Long businessId,Integer source, Integer resultCode) {
        log.info("群发任务回调：businessId:{},source:{},resultCode:{}", businessId,source,resultCode);
        applicationEventPublisher.publishEvent(new WeTaskEventQuery(this, businessId,source,resultCode));
    }
}
