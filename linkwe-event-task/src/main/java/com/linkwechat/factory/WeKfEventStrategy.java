package com.linkwechat.factory;

import com.alibaba.fastjson.JSONObject;

/**
 * @author danmo
 * @description 客服事件类型策略接口
 * @date 2021/1/20 22:00
 **/
public abstract class WeKfEventStrategy {

    public abstract void eventHandle(JSONObject message);
}
