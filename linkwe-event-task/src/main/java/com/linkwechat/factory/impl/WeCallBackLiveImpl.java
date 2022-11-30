package com.linkwechat.factory.impl;

import cn.hutool.core.util.XmlUtil;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.live.WeLive;
import com.linkwechat.domain.wecom.callback.WeBackLiveVo;
import com.linkwechat.factory.WeCallBackEventFactory;
import com.linkwechat.service.IWeLiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class WeCallBackLiveImpl implements WeCallBackEventFactory {

    @Autowired
    IWeLiveService iWeLiveService;

    @Override
    public void eventHandle(String message) {
        WeBackLiveVo weBackLiveVo = XmlUtil.xmlToBean(XmlUtil.parseXml(message).getFirstChild(), WeBackLiveVo.class);
        if(StringUtils.isNotEmpty(weBackLiveVo.getLivingId())){
            iWeLiveService.synchLiveData(
                    iWeLiveService.findLives(WeLive.builder()
                            .livingId(weBackLiveVo.getLivingId())
                            .delFlag(Constants.COMMON_STATE)
                            .build())
            );
        }
    }
}
