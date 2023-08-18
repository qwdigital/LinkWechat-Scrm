package com.linkwechat.factory.impl.customer;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.MessageConstants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.MessageNoticeType;
import com.linkwechat.common.enums.TrackState;
import com.linkwechat.common.enums.message.MessageTypeEnum;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.domain.wecom.callback.WeBackCustomerVo;
import com.linkwechat.factory.WeEventStrategy;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author danmo
 * @description 删除跟进成员事件
 * @date 2021/1/20 23:36
 **/
@Slf4j
@Component("del_follow_user")
public class WeCallBackDelFollowUserImpl extends WeEventStrategy {

    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Autowired
    private IWeCustomerTrajectoryService iWeCustomerTrajectoryService;


    @Autowired
    private IWeSopExecuteTargetService iWeSopExecuteTargetService;
    @Resource
    private IWeMessageNotificationService weMessageNotificationService;

    @Autowired
    private IWeMessagePushService iWeMessagePushService;

    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackCustomerVo customerInfo = (WeBackCustomerVo) message;
        WeCustomer weCustomer = weCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getAddUserId,customerInfo.getUserID())
                .eq(WeCustomer::getExternalUserid,customerInfo.getExternalUserID()).eq(WeCustomer::getDelFlag,0).last("limit 1"));
        if(weCustomer == null){
            return;
        }
        weCustomer.setTrackState(TrackState.STATE_YLS.getType());
        if(weCustomerService.updateById(weCustomer)){
            //异常结束当前客户涉及到的sop任务
            iWeSopExecuteTargetService.sopExceptionEnd(customerInfo.getExternalUserID());
            //添加跟进动态
            iWeCustomerTrajectoryService.createAddOrRemoveTrajectory(weCustomer.getExternalUserid(), weCustomer.getAddUserId(), false, true);

            String customerChurnNoticeSwitch = weCorpAccountService.getCustomerChurnNoticeSwitch();

            if (WeConstans.DEL_FOLLOW_USER_SWITCH_OPEN.equals(customerChurnNoticeSwitch)) {
                iWeMessagePushService.pushMessageSelfH5(ListUtil.toList(customerInfo.getUserID()), "【客户动态】<br/> <br/> 客户@" + weCustomer.getCustomerName() + "刚刚删除了您",
                        MessageNoticeType.DELETEWEUSER.getType(), true);

                //添加消息通知
                weMessageNotificationService.save(MessageTypeEnum.CUSTOMER.getType(), MessageConstants.CUSTOMER_DELETE, weCustomer.getCustomerName());
            }
        }
    }
}
