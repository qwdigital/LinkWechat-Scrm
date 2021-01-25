package com.linkwechat.wecom.factory.impl;

import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.WeGroupCodeActual;
import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeCallBackEventFactory;
import com.linkwechat.wecom.service.IWeGroupCodeActualService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 客户群变更回调事件处理
 */
@Service
@Slf4j
public class WeEventChangeExternalChatImpl implements WeCallBackEventFactory {

    @Autowired
    private IWeGroupCodeActualService weGroupCodeActualService;

    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        String changeType = message.getChangeType();
        // 客户群状态变更(群名变更，群成员增加或移除，群主变更，群公告变更)
        if (changeType.equals("update")) {
            WeGroupCodeActual weGroupCodeActual = weGroupCodeActualService.selectActualCodeByChatId(message.getChatId());
            if (StringUtils.isNotNull(weGroupCodeActual))
            {
                // 扫码次数+1
                weGroupCodeActual.setScanCodeTimes(weGroupCodeActual.getScanCodeTimes() + 1);
                if (weGroupCodeActual.getScanCodeTimes().equals(weGroupCodeActual.getScanCodeTimesLimit()))
                {
                    weGroupCodeActual.setStatus(WeConstans.WE_GROUP_CODE_DISABLE.longValue());
                    log.info("客户群 " + weGroupCodeActual.getGroupName() + " 二维码扫描次数已达上限");
                }
                weGroupCodeActualService.updateWeGroupCodeActual(weGroupCodeActual);
            }
        }
    }
}
