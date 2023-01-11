package com.linkwechat.factory.impl.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.domain.wecom.callback.WeBackCustomerVo;
import com.linkwechat.factory.WeEventStrategy;
import com.linkwechat.service.IWeCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author danmo
 * @description 客户同意进行聊天内容存档事件回调
 * @date 2021/1/21 1:24
 **/
@Slf4j
@Component("msg_audit_approved")
public class WeCallBackMsgAuditApprovedImpl extends WeEventStrategy {

    @Autowired
    private IWeCustomerService weCustomerService;

    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackCustomerVo customerInfo = (WeBackCustomerVo) message;
        WeCustomer weCustomer = weCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getAddUserId,customerInfo.getUserID())
                .eq(WeCustomer::getExternalUserid,customerInfo.getExternalUserID()).eq(WeCustomer::getDelFlag,0).last("limit 1"));
        if(weCustomer == null){
            return;
        }
        weCustomer.setIsOpenChat(1);
        weCustomer.setOpenChatTime(new Date(customerInfo.getCreateTime() * 1000));
        weCustomerService.updateById(weCustomer);
    }
}