package com.linkwechat.wecom.factory.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.Threads;
import com.linkwechat.wecom.client.WeMessagePushClient;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.WeEmpleCodeDto;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import com.linkwechat.wecom.domain.dto.WeMessagePushDto;
import com.linkwechat.wecom.domain.dto.WeWelcomeMsg;
import com.linkwechat.wecom.domain.dto.message.TextMessageDto;
import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeCallBackEventFactory;
import com.linkwechat.wecom.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 外部联系人回调事件处理
 * @date 2020/11/9 14:52
 **/
@Service
@Slf4j
public class WeEventChangeExternalContactImpl implements WeCallBackEventFactory {
    @Autowired
    private IWeEmpleCodeService weEmpleCodeService;
    @Autowired
    private IWeEmpleCodeTagService weEmpleCodeTagService;
    @Autowired
    private IWeCustomerService weCustomerService;
    @Autowired
    private IWeFlowerCustomerRelService weFlowerCustomerRelService;
    @Autowired
    private IWeFlowerCustomerTagRelService weFlowerCustomerTagRelService;
    @Autowired
    private IWeMaterialService weMaterialService;
    @Autowired
    private WeMessagePushClient weMessagePushClient;
    @Autowired
    private IWeCorpAccountService weCorpAccountService;
    @Autowired
    private IWeChatContactMappingService weChatContactMappingService;


    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        String changeType = message.getChangeType();
        switch (changeType) {
            case "add_external_contact"://添加企业客户事件
                addExternalContact(message);
                break;
            case "edit_external_contact"://编辑企业客户事件
                editExternalContact(message);
                break;
            case "add_half_external_contact"://外部联系人免验证添加成员事件
                addHalfExternalContact(message);
                break;
            case "del_external_contact"://删除企业客户事件
                delExternalContact(message);
                break;
            case "del_follow_user"://删除跟进成员事件
                delFollowUser(message);
                break;
            case "transfer_fail"://客户接替失败事件
                transferFail(message);
                break;
            case "msg_audit_approved"://客户同意进行聊天内容存档事件
                msgAuditApproved(message);
                break;
            default:
                break;
        }
        String chatId = message.getChatId();
        if (StringUtils.isNotEmpty(chatId)) {
            //客户群变更事件
            weChatChangeEvent(message);
        }
    }

    private void msgAuditApproved(WxCpXmlMessageVO message) {
        String userId = message.getUserId();
        String externalUserId = message.getExternalUserId();
        WeChatContactMapping fromMapping = new WeChatContactMapping();
        fromMapping.setFromId(userId);
        fromMapping.setReceiveId(externalUserId);
        fromMapping.setIsCustom(WeConstans.ID_TYPE_EX);
        weChatContactMappingService.insertWeChatContactMapping(fromMapping);
        WeChatContactMapping receiveMapping = new WeChatContactMapping();
        receiveMapping.setFromId(externalUserId);
        receiveMapping.setReceiveId(userId);
        receiveMapping.setIsCustom(WeConstans.ID_TYPE_USER);
        weChatContactMappingService.insertWeChatContactMapping(receiveMapping);

        weCustomerService.updateCustomerChatStatus(externalUserId);
    }

    private void weChatChangeEvent(WxCpXmlMessageVO message) {
    }

    private void transferFail(WxCpXmlMessageVO message) {
    }

    private void delFollowUser(WxCpXmlMessageVO message) {
        Threads.SINGLE_THREAD_POOL.execute(new Runnable() {
            @Override
            public void run() {
                if (message.getUserId() != null && message.getExternalUserId() != null) {
                    weFlowerCustomerRelService.deleteFollowUser(message.getUserId(), message.getExternalUserId());
                    WeCorpAccount validWeCorpAccount = weCorpAccountService.findValidWeCorpAccount();
                    Optional.ofNullable(validWeCorpAccount).ifPresent(weCorpAccount -> {
                        String customerChurnNoticeSwitch = weCorpAccount.getCustomerChurnNoticeSwitch();
                        if (WeConstans.DEL_FOLLOW_USER_SWITCH_OPEN.equals(customerChurnNoticeSwitch)){
                            WeCustomer weCustomer = weCustomerService.getById(message.getExternalUserId());
                            String content = "您已经被客户@"+weCustomer.getName()+"删除!" ;
                            TextMessageDto textMessageDto = new TextMessageDto();
                            textMessageDto.setContent(content);
                            List<String> userIdList = Arrays.stream(message.getUserId().split(",")).collect(Collectors.toList());
                            WeMessagePushDto weMessagePushDto = new WeMessagePushDto();
                            weMessagePushDto.setMsgtype(MessageType.TEXT.getMessageType());
                            weMessagePushDto.setTouser(userIdList);
                            weMessagePushDto.setText(textMessageDto);
                            Optional.ofNullable(validWeCorpAccount).map(WeCorpAccount::getAgentId).ifPresent(agentId -> {
                                weMessagePushDto.setAgentid(Integer.valueOf(agentId));
                            });
                            weMessagePushClient.sendMessageToUser(weMessagePushDto);
                        }
                    });
                }
            }
        });
    }

    private void delExternalContact(WxCpXmlMessageVO message) {
        if (message.getExternalUserId() != null) {
            weCustomerService.deleteCustomersByEid(message.getExternalUserId());
        }
    }

    private void addHalfExternalContact(WxCpXmlMessageVO message) {
        if (message.getExternalUserId() != null) {
            weCustomerService.getCustomersInfoAndSynchWeCustomer(message.getExternalUserId());
        }
    }

    private void editExternalContact(WxCpXmlMessageVO message) {
        if (message.getExternalUserId() != null) {
            weCustomerService.getCustomersInfoAndSynchWeCustomer(message.getExternalUserId());
        }
    }

    private void addExternalContact(WxCpXmlMessageVO message) {

        try {
            Threads.SINGLE_THREAD_POOL.submit(new Runnable() {
                @Override
                public void run() {
                    if (message.getExternalUserId() != null) {
                        weCustomerService.getCustomersInfoAndSynchWeCustomer(message.getExternalUserId());
                    }

                    //向扫码客户发送欢迎语
                    if (message.getState() != null && message.getWelcomeCode() != null) {
                        log.info("执行发送欢迎语>>>>>>>>>>>>>>>");
                        WeWelcomeMsg.WeWelcomeMsgBuilder weWelcomeMsgBuilder = WeWelcomeMsg.builder().welcome_code(message.getWelcomeCode());
                        WeEmpleCodeDto messageMap = weEmpleCodeService.selectWelcomeMsgByActivityScene(message.getState(),message.getUserId());
                        String empleCodeId = messageMap.getEmpleCodeId();
                        //查询活码对应标签
                        List<WeEmpleCodeTag> tagList = weEmpleCodeTagService.list(new LambdaQueryWrapper<WeEmpleCodeTag>()
                                .eq(WeEmpleCodeTag::getEmpleCodeId, empleCodeId));
                        //查询外部联系人与通讯录关系数据
                        WeFlowerCustomerRel weFlowerCustomerRel = weFlowerCustomerRelService.getOne(new LambdaQueryWrapper<WeFlowerCustomerRel>()
                                .eq(WeFlowerCustomerRel::getUserId, message.getUserId())
                                .eq(WeFlowerCustomerRel::getExternalUserid, message.getExternalUserId()));
                        //为外部联系人添加员工活码标签
                        List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels = new ArrayList<>();
                        Optional.ofNullable(weFlowerCustomerRel).ifPresent(weFlowerCustomerRel1 -> {
                            Optional.ofNullable(tagList).orElseGet(ArrayList::new).forEach(tag ->{
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
                            if (StringUtils.isNotEmpty(messageMap.getWelcomeMsg())){
                                weWelcomeMsgBuilder.text(WeWelcomeMsg.Text.builder()
                                        .content(messageMap.getWelcomeMsg()).build());
                            }
                            if(StringUtils.isNotEmpty(messageMap.getCategoryId())){
                                WeMediaDto weMediaDto = weMaterialService
                                        .uploadTemporaryMaterial(messageMap.getMaterialUrl(),messageMap.getMaterialName(), MediaType.IMAGE.getMediaType());
                                Optional.ofNullable(weMediaDto).ifPresent(media ->{
                                    weWelcomeMsgBuilder.image(WeWelcomeMsg.Image.builder().media_id(media.getMedia_id())
                                            .pic_url(media.getUrl()).build());
                                });

                            }
                            weCustomerService.sendWelcomeMsg(weWelcomeMsgBuilder.build());
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error("执行发送欢迎语失败！",e);
        }
    }
}
