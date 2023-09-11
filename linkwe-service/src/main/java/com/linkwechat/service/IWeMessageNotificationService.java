package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.message.entity.WeMessageNotification;

/**
 * 消息通知 服务类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/20 14:58
 */
public interface IWeMessageNotificationService extends IService<WeMessageNotification> {

    /**
     * 保存消息通知
     *
     * @param title   消息标题
     * @param userId  用户Id
     * @param content 消息内容
     * @param params  参数集合
     * @return {@link Long} 消息Id
     * @author WangYX
     * @date 2023/07/20 14:58
     */
    WeMessageNotification save(String title, String userId, String content, String... params);

    /**
     * 保存消息并发送企微应用消息
     *
     * @param title    消息标题
     * @param weUserId 用户Id
     * @param content  消息内容
     * @param params   参数集合
     * @return {@link Long} 消息Id
     * @author WangYX
     * @date 2023/07/20 15:40
     */
    Long saveAndSend(String title, String weUserId, String content, String... params);
}
