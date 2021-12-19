package com.linkwechat.wecom.factory.impl.customer;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.callback.WeBackBaseVo;
import com.linkwechat.wecom.domain.callback.WeBackCustomerVo;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import com.linkwechat.wecom.domain.dto.WeWelcomeMsg;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.*;
import com.linkwechat.wecom.service.event.WeEventPublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author danmo
 * @description 新增客户事件
 * @date 2021/1/20 23:18
 **/
@Slf4j
@Component("add_external_contact")
public class WeCallBackAddExternalContactImpl extends WeEventStrategy {
    @Autowired
    private IWeCustomerService weCustomerService;
    @Autowired
    private IWeMaterialService weMaterialService;
    @Autowired
    private IWeTaskFissionRecordService weTaskFissionRecordService;
    @Autowired
    private IWeTaskFissionRewardService weTaskFissionRewardService;
    @Autowired
    private IWeTaskFissionService weTaskFissionService;

    @Autowired
    private WeEventPublisherService weEventPublisherService;


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
            fissNum = fissNum + 1;
            weTaskFissionRecord.setFissNum(fissNum);
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

    /**
     * 构建欢迎语的图片部分
     *
     * @param builder  欢迎语builder
     * @param picUrl   图片链接
     * @param fileName 图片名称
     */
    private void buildWelcomeMsgImg(WeWelcomeMsg.WeWelcomeMsgBuilder builder, String picUrl, String fileName) {
        WeMediaDto weMediaDto = weMaterialService.uploadTemporaryMaterial(picUrl, MediaType.IMAGE.getMediaType(), fileName);
        Optional.ofNullable(weMediaDto).ifPresent(media -> {
            builder.image(WeWelcomeMsg.Image.builder().media_id(media.getMedia_id()).pic_url(media.getUrl()).build());
        });
    }
}
