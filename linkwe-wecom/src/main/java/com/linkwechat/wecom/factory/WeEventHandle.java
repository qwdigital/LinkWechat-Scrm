package com.linkwechat.wecom.factory;

import com.linkwechat.common.constant.WeConstans;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author danmo
 * @description 企微回调策略
 * @date 2020/11/8 17:17
 **/
@Service
public class WeEventHandle {

    @Autowired
    private Map<String, WeCallBackEventFactory> weCallBackEventFactoryMap;

    public WeCallBackEventFactory factory(String eventType) {
        if (!WeConstans.eventRoute.containsKey(eventType)) {
            return null;
        }
        String resolveClass = WeConstans.eventRoute.get(eventType);
        if (!weCallBackEventFactoryMap.containsKey(resolveClass)) {
            return null;
        }
        return weCallBackEventFactoryMap.get(resolveClass);
    }
}
