package com.linkwechat.factory.impl;

import cn.hutool.core.util.XmlUtil;
import com.linkwechat.domain.wecom.callback.WeBackCustomerVo;
import com.linkwechat.factory.WeCallBackEventFactory;
import com.linkwechat.factory.WeStrategyBeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author danmo
 * @description 外部联系人回调事件处理
 * @date 2020/11/9 14:52
 **/
@Service
@Slf4j
public class WeEventChangeExternalContactImpl implements WeCallBackEventFactory {
    @Autowired
    private WeStrategyBeanFactory weStrategyBeanFactory;

    @Override
    public void eventHandle(String message) {
        WeBackCustomerVo weBackCustomerVo = XmlUtil.xmlToBean(XmlUtil.parseXml(message).getFirstChild(), WeBackCustomerVo.class);
        String changeType = weBackCustomerVo.getChangeType();
        weStrategyBeanFactory.getResource(changeType, weBackCustomerVo);
    }
}
