package com.linkwechat.wecom.factory.impl.customergroup;

import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.IWeGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 客户群创建事件
 * @date 2021/1/20 0:28
 **/
@Slf4j
@Component("create")
public class WeCallBackCreateGroupImpl extends WeEventStrategy {

    @Autowired
    private IWeGroupService weGroupService;

    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        try {
            weGroupService.createWeGroup(message.getChatId());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("create>>>>>>>>>param:{},ex:{}",message.getChatId(),e);
        }
    }
}
