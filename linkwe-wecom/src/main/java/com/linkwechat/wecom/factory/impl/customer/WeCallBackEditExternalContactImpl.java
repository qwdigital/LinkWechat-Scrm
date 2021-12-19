package com.linkwechat.wecom.factory.impl.customer;

import com.linkwechat.wecom.domain.callback.WeBackBaseVo;
import com.linkwechat.wecom.domain.callback.WeBackCustomerVo;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.IWeCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 编辑客户事件
 * @date 2021/1/20 23:25
 **/
@Slf4j
@Component("edit_external_contact")
public class WeCallBackEditExternalContactImpl extends WeEventStrategy {
    @Autowired
    private IWeCustomerService weCustomerService;

    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackCustomerVo customerInfo = (WeBackCustomerVo) message;
        if (customerInfo.getExternalUserID() != null) {
            weCustomerService.getCustomersInfoAndSynchWeCustomer(customerInfo.getExternalUserID(),customerInfo.getUserID());
        }
    }
}
