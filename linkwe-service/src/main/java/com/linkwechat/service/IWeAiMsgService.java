package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeAiMsg;

import java.util.List;

/**
 * ai助手消息表(WeAiMsg)
 *
 * @author makejava
 * @since 2023-12-01 15:12:13
 */
public interface IWeAiMsgService extends IService<WeAiMsg> {

    List<WeAiMsg> getSessionList(Long userId, String content, Integer collection);

    void collectionMsg(Long id);
}
