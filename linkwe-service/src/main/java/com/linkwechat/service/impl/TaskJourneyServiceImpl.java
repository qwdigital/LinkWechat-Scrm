package com.linkwechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.service.AbstractGroupMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author danmo
 * @description 策略旅程回调状态
 * @date 2022/4/14 23:45
 **/
@Service("TaskJourneyService")
@Slf4j
public class TaskJourneyServiceImpl extends AbstractGroupMsgService {


    @Override
    public void callBackHandler(JSONObject query) {
        String businessId = query.getString("businessId");
        Integer status = query.getInteger("status");

    }
}
