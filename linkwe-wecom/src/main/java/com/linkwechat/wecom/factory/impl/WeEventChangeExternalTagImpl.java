package com.linkwechat.wecom.factory.impl;

import cn.hutool.core.util.XmlUtil;
import com.linkwechat.wecom.domain.callback.WeBackCustomerGroupVo;
import com.linkwechat.wecom.domain.callback.WeBackCustomerTagVo;
import com.linkwechat.wecom.domain.callback.WeBackCustomerVo;
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
    public void eventHandle(String message) {
        WeBackCustomerTagVo weBackCustomerTagVo = XmlUtil.xmlToBean(XmlUtil.parseXml(message).getFirstChild(), WeBackCustomerTagVo.class);
        String changeType = weBackCustomerTagVo.getChangeType()+"CustomerTag";
        weStrategyBeanFactory.getResource(changeType,weBackCustomerTagVo);
    }
}
