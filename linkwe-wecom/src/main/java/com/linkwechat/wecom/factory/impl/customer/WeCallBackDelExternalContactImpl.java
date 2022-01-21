package com.linkwechat.wecom.factory.impl.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.wecom.domain.callback.WeBackBaseVo;
import com.linkwechat.wecom.domain.callback.WeBackCustomerVo;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeSensitiveAct;
import com.linkwechat.wecom.domain.WeSensitiveActHit;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.IWeCustomerService;
import com.linkwechat.wecom.service.IWeSensitiveActHitService;
import com.linkwechat.wecom.service.IWeUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author danmo
 * @description 删除企业客户事件
 * @date 2021/1/20 23:33
 **/
@Slf4j
@Component("del_external_contact")
public class WeCallBackDelExternalContactImpl extends WeEventStrategy {
    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeSensitiveActHitService weSensitiveActHitService;
    @Autowired
    private IWeUserService weUserService;

    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackCustomerVo customerInfo = (WeBackCustomerVo) message;
        if (customerInfo.getExternalUserID() != null) {
            WeCustomer weCustomer = weCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getFirstUserId,customerInfo.getUserID())
                    .eq(WeCustomer::getExternalUserid,customerInfo.getExternalUserID()).eq(WeCustomer::getDelFlag,0).last("limit 1"));
            if(weCustomer == null){
                return;
            }
            //删除客户
            WeCustomer customer = new WeCustomer();
            customer.setDelFlag(1);
            weCustomerService.update(customer,new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getFirstUserId,customerInfo.getUserID())
                    .eq(WeCustomer::getExternalUserid,customerInfo.getExternalUserID()).eq(WeCustomer::getDelFlag,0));
            //增加敏感行为记录，员工删除客户
            WeSensitiveAct weSensitiveAct = weSensitiveActHitService.getSensitiveActType("拉黑/删除好友");
            if (weSensitiveAct != null && weSensitiveAct.getEnableFlag() == 1) {
                WeSensitiveActHit weSensitiveActHit = new WeSensitiveActHit();
                weSensitiveActHit.setSensitiveActId(weSensitiveAct.getId());
                weSensitiveActHit.setSensitiveAct(weSensitiveAct.getActName());
                weSensitiveActHit.setCreateTime(new Date(customerInfo.getCreateTime()));
                weSensitiveActHit.setCreateBy("admin");
                weSensitiveActHit.setOperatorId(customerInfo.getUserID());
                WeUser user = weUserService.getById(customerInfo.getUserID());
                weSensitiveActHit.setOperator(user.getName());
                weSensitiveActHit.setOperateTargetId(weCustomer.getFirstUserId());
                weSensitiveActHit.setOperateTarget(weCustomer.getCustomerName());
                weSensitiveActHitService.insertWeSensitiveActHit(weSensitiveActHit);
            }
        }
    }
}
