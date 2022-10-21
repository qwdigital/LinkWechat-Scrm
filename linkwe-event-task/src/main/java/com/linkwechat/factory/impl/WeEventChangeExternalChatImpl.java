package com.linkwechat.factory.impl;

import cn.hutool.core.util.XmlUtil;
import com.linkwechat.domain.wecom.callback.WeBackCustomerGroupVo;
import com.linkwechat.factory.WeCallBackEventFactory;
import com.linkwechat.factory.WeStrategyBeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author danmo
 * @description 客户群事件
 * @date 2021/1/20 1:15
 **/

@Service
@Slf4j
public class WeEventChangeExternalChatImpl implements WeCallBackEventFactory {
    @Autowired
    private WeStrategyBeanFactory weStrategyBeanFactory;

    @Override
    public void eventHandle(String message) {
        WeBackCustomerGroupVo weBackCustomerGroupVo = XmlUtil.xmlToBean(XmlUtil.parseXml(message).getFirstChild(), WeBackCustomerGroupVo.class);
        String changeType = weBackCustomerGroupVo.getChangeType();
        weStrategyBeanFactory.getResource(changeType,weBackCustomerGroupVo);
    }
}
