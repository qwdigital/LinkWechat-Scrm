package com.linkwechat.wecom.factory.impl;

import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeCallBackEventFactory;
import com.linkwechat.wecom.factory.WeStrategyBeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author danmo
 * @description 客户标签事件
 * @date 2021/1/20 1:13
 **/
@Service
@Slf4j
public class WeEventChangeExternalTagImpl implements WeCallBackEventFactory {
    @Autowired
    private WeStrategyBeanFactory weStrategyBeanFactory;

    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        String changeType = message.getChangeType()+"CustomerTag";
        weStrategyBeanFactory.getResource(changeType,message);
    }
}
