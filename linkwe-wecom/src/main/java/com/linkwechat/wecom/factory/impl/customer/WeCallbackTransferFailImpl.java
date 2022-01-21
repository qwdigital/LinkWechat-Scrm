package com.linkwechat.wecom.factory.impl.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.enums.TransferFailReason;
import com.linkwechat.wecom.domain.callback.WeBackBaseVo;
import com.linkwechat.wecom.domain.callback.WeBackCustomerVo;
import com.linkwechat.wecom.domain.WeAllocateCustomer;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.IWeAllocateCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 客户接替失败事件
 * @date 2021/1/20 23:41
 **/
@Slf4j
@Component("transfer_fail")
public class WeCallbackTransferFailImpl extends WeEventStrategy {

    @Autowired
    private IWeAllocateCustomerService iWeAllocateCustomerService;

    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackCustomerVo customerInfo = (WeBackCustomerVo) message;
        String userId = customerInfo.getUserID();
        String externalUserId = customerInfo.getExternalUserID();
        String failReason = customerInfo.getFailReason();
        WeAllocateCustomer weAllocateCustomer = WeAllocateCustomer.builder().failReason(TransferFailReason.getReason(failReason))
                .status(TransferFailReason.getNum(failReason)).build();
        iWeAllocateCustomerService.update(weAllocateCustomer,new LambdaQueryWrapper<WeAllocateCustomer>().eq(WeAllocateCustomer::getTakeoverUserid,userId)
                .eq(WeAllocateCustomer::getExternalUserid,externalUserId));
    }

}
