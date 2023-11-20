package com.linkwechat.factory.impl;

import cn.hutool.core.util.XmlUtil;
import com.linkwechat.domain.wecom.callback.WeBackCustomerTagVo;
import com.linkwechat.domain.wecom.callback.WeBackUserTagVo;
import com.linkwechat.factory.WeCallBackEventFactory;
import com.linkwechat.factory.WeStrategyBeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        if("shuffle".equals(weBackCustomerTagVo.getChangeType())){
            WeBackUserTagVo weBackUserTagVo = XmlUtil.xmlToBean(XmlUtil.parseXml(message).getFirstChild(), WeBackUserTagVo.class);
            weStrategyBeanFactory.getResource(changeType,weBackUserTagVo);
        }else{

            weStrategyBeanFactory.getResource(changeType,weBackCustomerTagVo);

        }




    }
}
