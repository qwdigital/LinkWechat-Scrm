package com.linkwechat.wecom.service.callback;

import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.wecom.domain.callback.WeBackBaseVo;
import com.linkwechat.wecom.domain.callback.WeCallBackEvent;
import com.linkwechat.wecom.factory.WeCallBackEventFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author danmo
 * @description 回调通知处理
 * @date 2021/11/21 19:57
 **/
@Service
@Slf4j
public class WeCallBackEventService implements ApplicationListener<WeCallBackEvent> {

    @Autowired
    private Map<String, WeCallBackEventFactory> weCallBackEventFactoryMap = new ConcurrentHashMap<>();

    /**
     * 企微回调事件类型路由
     */
    private final Map<String, String> eventRoute = new HashMap<String, String>() {
        {
            //成员事件
            put("change_contact", "weEventChangeContactImpl");
            //异步任务完成通知
            put("batch_job_result", "weEventBatchJobResultImpl");
            //外部联系人事件
            put("change_external_contact", "weEventChangeExternalContactImpl");
            //客户群事件
            put("change_external_chat", "weEventChangeExternalChatImpl");
            //客户标签事件
            put("change_external_tag", "weEventChangeExternalTagImpl");
        }
    };


    @Override
    @Async
    public void onApplicationEvent(WeCallBackEvent weCallBackEvent) {
        String message = weCallBackEvent.getMessage();
        WeBackBaseVo weBackBaseVo = XmlUtil.xmlToBean(XmlUtil.parseXml(message).getFirstChild(), WeBackBaseVo.class);
        log.info("回调通知处理：weBackBaseVo:{}", JSONObject.toJSONString(weBackBaseVo));
        WeCallBackEventFactory factory = factory(weBackBaseVo.getEvent());
        if(factory != null){
            factory.eventHandle(message);
        }
    }

    private WeCallBackEventFactory factory(String eventType) {
        if (!eventRoute.containsKey(eventType)) {
            return null;
        }
        String resolveClass = eventRoute.get(eventType);
        if (!weCallBackEventFactoryMap.containsKey(resolveClass)) {
            return null;
        }
        return weCallBackEventFactoryMap.get(resolveClass);
    }

}
