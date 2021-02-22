package com.linkwechat.wecom.factory.impl.customergroup;

import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.IWeGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 客户群解散事件
 * @date 2021/1/20 1:07
 **/
@Slf4j
@Component("dismiss")
public class WeCallBackDismissImpl extends WeEventStrategy {

    @Autowired
    private IWeGroupService weGroupService;

    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        try {
            weGroupService.deleteWeGroup(message.getChatId());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("dismiss>>>>>>>>>param:{},ex:{}",message.getChatId(),e);
        }
    }
}
