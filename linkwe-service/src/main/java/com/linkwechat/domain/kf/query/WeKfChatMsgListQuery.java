package com.linkwechat.domain.kf.query;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 客服会话消息
 * @date 2022/5/4 21:52
 **/
@Data
public class WeKfChatMsgListQuery {

    private List<JSONObject> msgList;

    private String corpId;
}
