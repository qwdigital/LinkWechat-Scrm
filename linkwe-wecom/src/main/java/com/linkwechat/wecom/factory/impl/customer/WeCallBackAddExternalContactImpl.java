package com.linkwechat.wecom.factory.impl.customer;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.WeEmpleCodeDto;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import com.linkwechat.wecom.domain.dto.WeWelcomeMsg;
import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private IWeEmpleCodeTagService weEmpleCodeTagService;
    @Autowired
    private IWeEmpleCodeService weEmpleCodeService;
    @Autowired
    private IWeFlowerCustomerRelService weFlowerCustomerRelService;
    @Autowired
    private IWeFlowerCustomerTagRelService weFlowerCustomerTagRelService;
    @Autowired
    private IWeMaterialService weMaterialService;
    @Autowired
    private IWeTaskFissionRecordService weTaskFissionRecordService;
    @Autowired
    private IWeTaskFissionRewardService weTaskFissionRewardService;
    @Autowired
    private IWeTaskFissionService weTaskFissionService;
    @Autowired
    private ThreadLocal<WeCustomer> weCustomerThreadLocal = new ThreadLocal<>();

    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        if (message.getExternalUserId() != null) {
            WeCustomer weCustomer = weCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getExternalUserid, message.getExternalUserId()));
            weCustomerThreadLocal.set(weCustomer);
            weCustomerService.getCustomersInfoAndSynchWeCustomer(message.getExternalUserId());
        }
        if (message.getState() != null && message.getWelcomeCode() != null) {
            if (isFission(message.getState())) {
                taskFissionRecordHandle(message.getState(), message.getWelcomeCode(), message.getUserId(), message.getExternalUserId());
            }else {
                empleCodeHandle(message.getState(), message.getWelcomeCode(), message.getUserId(), message.getExternalUserId());
            }
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
            if (weCustomerThreadLocal.get() != null){
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

    /**
     * 活码欢迎语发送
     *
     * @param state          渠道
     * @param wecomCode      欢迎语code
     * @param userId         成员id
     * @param externalUserId 客户id
     */
    private void empleCodeHandle(String state, String wecomCode, String userId, String externalUserId) {
        try {
            log.info("执行发送欢迎语>>>>>>>>>>>>>>>");
            WeWelcomeMsg.WeWelcomeMsgBuilder weWelcomeMsgBuilder = WeWelcomeMsg.builder().welcome_code(wecomCode);
            WeEmpleCodeDto messageMap = weEmpleCodeService.selectWelcomeMsgByActivityScene(state, userId);
            if (messageMap != null) {
                String empleCodeId = messageMap.getEmpleCodeId();
                //查询活码对应标签
                List<WeEmpleCodeTag> tagList = weEmpleCodeTagService.list(new LambdaQueryWrapper<WeEmpleCodeTag>()
                        .eq(WeEmpleCodeTag::getEmpleCodeId, empleCodeId));
                //查询外部联系人与通讯录关系数据
                WeFlowerCustomerRel weFlowerCustomerRel = weFlowerCustomerRelService.getOne(new LambdaQueryWrapper<WeFlowerCustomerRel>()
                        .eq(WeFlowerCustomerRel::getUserId, userId)
                        .eq(WeFlowerCustomerRel::getExternalUserid, externalUserId));
                //为外部联系人添加员工活码标签
                List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels = new ArrayList<>();
                Optional.ofNullable(weFlowerCustomerRel).ifPresent(weFlowerCustomerRel1 -> {
                    Optional.ofNullable(tagList).orElseGet(ArrayList::new).forEach(tag -> {
                        weFlowerCustomerTagRels.add(
                                WeFlowerCustomerTagRel.builder()
                                        .flowerCustomerRelId(weFlowerCustomerRel.getId())
                                        .tagId(tag.getTagId())
                                        .createTime(new Date())
                                        .build()
                        );
                    });
                    weFlowerCustomerTagRelService.saveOrUpdateBatch(weFlowerCustomerTagRels);
                });
                log.debug(">>>>>>>>>欢迎语查询结果：{}", JSONObject.toJSONString(messageMap));
                if (messageMap != null) {
                    if (StringUtils.isNotEmpty(messageMap.getWelcomeMsg())) {
                        weWelcomeMsgBuilder.text(WeWelcomeMsg.Text.builder()
                                .content(messageMap.getWelcomeMsg()).build());
                    }
                    if (StringUtils.isNotEmpty(messageMap.getCategoryId())) {
                        WeMediaDto weMediaDto = weMaterialService
                                .uploadTemporaryMaterial(messageMap.getMaterialUrl(), MediaType.IMAGE.getMediaType(),messageMap.getMaterialName());
                        Optional.ofNullable(weMediaDto).ifPresent(media -> {
                            weWelcomeMsgBuilder.image(WeWelcomeMsg.Image.builder().media_id(media.getMedia_id())
                                    .pic_url(media.getUrl()).build());
                        });
                    }
                    weCustomerService.sendWelcomeMsg(weWelcomeMsgBuilder.build());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("执行发送欢迎语失败！", e);
        }
    }

    private boolean isFission(String str) {
        if (str.indexOf(WeConstans.FISSION_PREFIX) != -1){
            return true;
        }
       return false;
    }


}
