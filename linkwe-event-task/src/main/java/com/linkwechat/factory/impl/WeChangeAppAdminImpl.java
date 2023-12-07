package com.linkwechat.factory.impl;

import cn.hutool.core.util.XmlUtil;
import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.factory.WeCallBackEventFactory;
import com.linkwechat.service.IWeCorpAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author danmo
 * @description 应用管理员变更通知
 * @date 2021/1/20 1:13
 **/
@Service
@Slf4j
public class WeChangeAppAdminImpl implements WeCallBackEventFactory {


    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Override
    public void eventHandle(String message) {
        WeBackBaseVo weBackBaseVo = XmlUtil.xmlToBean(XmlUtil.parseXml(message).getFirstChild(), WeBackBaseVo.class);
        log.info("应用管理员变更");
        String corpId = weBackBaseVo.getToUserName();
    }

}
