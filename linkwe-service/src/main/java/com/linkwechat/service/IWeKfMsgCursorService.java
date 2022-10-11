package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeKfMsgCursor;

/**
 * 客服消息偏移量表(WeKfMsgCursor)
 *
 * @author danmo
 * @since 2022-04-15 15:53:37
 */
public interface IWeKfMsgCursorService extends IService<WeKfMsgCursor> {

    /**
     * 获取会话偏移量
     * @return
     * @param corpId 企业ID
     */
    String getKfMsgCursor(String corpId);

    /**
     * 保存会话偏移量
     * @param corpId 企业ID
     * @param nextCursor 偏移量
     */
    void saveKfMsgCursor(String corpId,String nextCursor);
} 
