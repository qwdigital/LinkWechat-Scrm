package com.linkwechat.wecom.factory.impl.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeSensitiveAct;
import com.linkwechat.wecom.domain.WeSensitiveActHit;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
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
    public void eventHandle(WxCpXmlMessageVO message) {
        if (message.getExternalUserId() != null) {

            //删除客户
            if(weCustomerService.remove(new LambdaQueryWrapper<WeCustomer>()
                    .eq(WeCustomer::getExternalUserid,message.getExternalUserId())
                    .eq(WeCustomer::getFirstUserId,message.getUserId()))){
                //增加敏感行为记录，员工删除客户
                WeSensitiveAct weSensitiveAct = weSensitiveActHitService.getSensitiveActType("拉黑/删除好友");
                if (weSensitiveAct != null && weSensitiveAct.getEnableFlag() == 1) {
                    WeSensitiveActHit weSensitiveActHit = new WeSensitiveActHit();
                    weSensitiveActHit.setSensitiveActId(weSensitiveAct.getId());
                    weSensitiveActHit.setSensitiveAct(weSensitiveAct.getActName());
                    weSensitiveActHit.setCreateTime(new Date(message.getCreateTime()));
                    weSensitiveActHit.setCreateBy("admin");
                    weSensitiveActHit.setOperatorId(message.getUserId());
                    WeUser user = weUserService.getById(message.getUserId());
                    weSensitiveActHit.setOperator(user.getName());

                    WeCustomer weCustomer = weCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>()
                            .eq(WeCustomer::getExternalUserid,message.getExternalUserId())
                            .eq(WeCustomer::getFirstUserId,message.getUserId()));

                    weSensitiveActHit.setOperateTargetId(weCustomer.getUserId());
                    weSensitiveActHit.setOperateTarget(weCustomer.getName());
                    weSensitiveActHitService.insertWeSensitiveActHit(weSensitiveActHit);
                }

            }

        }
    }
}
