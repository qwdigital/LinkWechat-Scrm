package com.linkwechat.wecom.factory.impl.customer;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.callback.WeBackBaseVo;
import com.linkwechat.wecom.domain.callback.WeBackCustomerVo;
import com.linkwechat.wecom.domain.WeFlowerCustomerRel;
import com.linkwechat.wecom.domain.WeTaskFission;
import com.linkwechat.wecom.domain.WeTaskFissionRecord;
import com.linkwechat.wecom.domain.WeTaskFissionReward;
import com.linkwechat.wecom.domain.dto.WeWelcomeMsg;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.IWeCustomerService;
import com.linkwechat.wecom.service.IWeTaskFissionRecordService;
import com.linkwechat.wecom.service.IWeTaskFissionRewardService;
import com.linkwechat.wecom.service.IWeTaskFissionService;
import com.linkwechat.wecom.service.event.WeEventPublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author danmo
 * @description 外部联系人免验证添加成员事件
 * @date 2021/1/20 23:28
 **/
@Slf4j
@Component("add_half_external_contact")
public class WeCallBackAddHalfExternalContactImpl extends WeEventStrategy {

    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeTaskFissionRecordService weTaskFissionRecordService;
    @Autowired
    private IWeTaskFissionRewardService weTaskFissionRewardService;
    @Autowired
    private IWeTaskFissionService weTaskFissionService;

    @Autowired
    private WeEventPublisherService weEventPublisherService;

    private ThreadLocal<WeFlowerCustomerRel> weFlowerCustomerRelThreadLocal = new ThreadLocal<>();


    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackCustomerVo customerInfo = (WeBackCustomerVo) message;
        if (customerInfo.getExternalUserID() != null) {
            weCustomerService.getCustomersInfoAndSynchWeCustomer(customerInfo.getExternalUserID(),customerInfo.getUserID());
        }

        weEventPublisherService.register(customerInfo.getExternalUserID(),customerInfo.getUserID(),customerInfo.getWelcomeCode(),customerInfo.getState());
        if (StringUtils.isNotEmpty(customerInfo.getState()) && isFission(customerInfo.getState())) {
            taskFissionRecordHandle(customerInfo.getState(), customerInfo.getWelcomeCode(), customerInfo.getUserID(), customerInfo.getExternalUserID());
        }
    }

    //裂变任务处理
    private void taskFissionRecordHandle(String state, String wecomCode, String userId, String externalUserId) {
        log.info("裂变任务处理  >>>>>>>>>>start");
        //查询裂变客户任务记录
        String fissionRecordId = state.substring(WeConstans.FISSION_PREFIX.length());
        WeTaskFissionRecord weTaskFissionRecord = weTaskFissionRecordService.selectWeTaskFissionRecordById(Long.valueOf(fissionRecordId));
        if (weTaskFissionRecord != null) {
            //查询裂变任务详情
            WeTaskFission weTaskFission = weTaskFissionService
                    .selectWeTaskFissionById(weTaskFissionRecord.getTaskFissionId());
            Long fissNum = weTaskFissionRecord.getFissNum();
            if (weFlowerCustomerRelThreadLocal.get() == null){
                fissNum++;
                weTaskFissionRecord.setFissNum(fissNum);
            }
            log.info("查询裂变任务详情  >>>>>>>>>>{}",JSONObject.toJSONString(weTaskFissionRecord));
            if (weTaskFission != null){
                //发送欢迎语
                WeWelcomeMsg.WeWelcomeMsgBuilder weWelcomeMsgBuilder = WeWelcomeMsg.builder().welcome_code(wecomCode);
                weWelcomeMsgBuilder.text(WeWelcomeMsg.Text.builder()
                        .content(weTaskFission.getWelcomeMsg()).build());
                weCustomerService.sendWelcomeMsg(weWelcomeMsgBuilder.build());
            }

            //裂变数量完成任务处理,消费兑换码
            if (fissNum >= weTaskFission.getFissNum()){
                log.info("裂变数量完成任务处理,消费兑换码  >>>>>>>>>>{}",fissNum);
                weTaskFissionRecord.setCompleteTime(new Date());
                WeTaskFissionReward reward = new WeTaskFissionReward();
                reward.setTaskFissionId(weTaskFissionRecord.getTaskFissionId());
                reward.setRewardCodeStatus(0);
                List<WeTaskFissionReward> weTaskFissionRewardList = weTaskFissionRewardService.selectWeTaskFissionRewardList(reward);
                WeTaskFissionReward fissionReward = weTaskFissionRewardList.get(0);
                fissionReward.setRewardUser(weTaskFissionRecord.getCustomerName());
                fissionReward.setRewardUserId(weTaskFissionRecord.getCustomerId());
                weTaskFissionRewardService.updateWeTaskFissionReward(fissionReward);
            }
            log.info("裂变任务处理变更  >>>>>>>>>>{}",JSONObject.toJSONString(weTaskFissionRecord));
            weTaskFissionRecordService.updateWeTaskFissionRecord(weTaskFissionRecord);
            log.info("裂变任务处理  >>>>>>>>>>end");
        }
    }

    private boolean isFission(String str) {
        return str.contains(WeConstans.FISSION_PREFIX);
    }
}
