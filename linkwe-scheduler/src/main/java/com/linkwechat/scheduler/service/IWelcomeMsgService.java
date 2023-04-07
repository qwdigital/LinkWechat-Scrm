package com.linkwechat.scheduler.service;

import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.wecom.callback.WeBackCustomerVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author danmo
 * @date 2023年03月10日 11:43
 */
@Service
public interface IWelcomeMsgService {

    /**
     * 发送企微消息
     * @param query 发送欢迎语参数
     * @param attachments 素材/附件
     */
    void sendWelcomeMsg(WeBackCustomerVo query, List<WeMessageTemplate> attachments);

    /**
     * 业务处理
     * @param query
     */
    void msgHandle(WeBackCustomerVo query);
}
