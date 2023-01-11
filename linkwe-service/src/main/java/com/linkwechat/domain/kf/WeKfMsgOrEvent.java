package com.linkwechat.domain.kf;

import com.alibaba.fastjson.JSONObject;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author danmo
 * @description
 * @date 2021/11/21 19:52
 **/
public class WeKfMsgOrEvent extends ApplicationEvent {

    private List<JSONObject> msgList;

    public WeKfMsgOrEvent(Object source, List<JSONObject> msgList) {
        super(source);
        this.msgList = msgList;
    }

    public List<JSONObject> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<JSONObject> msgList) {
        this.msgList = msgList;
    }
}
