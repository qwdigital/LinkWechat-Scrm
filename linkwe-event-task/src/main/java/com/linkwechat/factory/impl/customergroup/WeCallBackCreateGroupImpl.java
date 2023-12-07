package com.linkwechat.factory.impl.customergroup;

import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.domain.wecom.callback.WeBackCustomerGroupVo;
import com.linkwechat.factory.WeEventStrategy;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.service.IWeGroupService;
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
    public void eventHandle(WeBackBaseVo message) {
        WeBackCustomerGroupVo customerGroupInfo = (WeBackCustomerGroupVo) message;
        try {
            weGroupService.createWeGroup(customerGroupInfo.getChatId(),message.getToUserName(),true);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("create>>>>>>>>>param:{}",customerGroupInfo.getChatId(),e);
        }finally {
            SecurityContextHolder.remove();
        }
    }
}
