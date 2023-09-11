package com.linkwechat.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeKfEventMsg;

import java.util.List;

/**
 * 客服事件消息表(WeKfEventMsg)
 *
 * @author danmo
 * @since 2022-04-15 15:53:35
 */
public interface IWeKfEventMsgService extends IService<WeKfEventMsg> {

    /**
     * 保存事件消息
     *
     * @param weKfEventMsgList
     */
    void saveEventMsg(List<JSONObject> weKfEventMsgList);
}
