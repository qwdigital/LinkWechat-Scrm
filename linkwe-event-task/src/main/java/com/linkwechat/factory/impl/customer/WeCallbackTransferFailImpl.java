package com.linkwechat.factory.impl.customer;

import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.domain.wecom.callback.WeBackCustomerVo;
import com.linkwechat.factory.WeEventStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 客户接替失败事件
 * @date 2021/1/20 23:41
 **/
@Slf4j
@Component("transfer_fail")
public class WeCallbackTransferFailImpl extends WeEventStrategy {

    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackCustomerVo customerInfo = (WeBackCustomerVo) message;
    }

}
