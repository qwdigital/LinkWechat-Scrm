package com.linkwechat.wecom.factory.impl.customer;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.entity.WeCorpAccount;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.wecom.client.WeMessagePushClient;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.WeMessagePushDto;
import com.linkwechat.wecom.domain.dto.message.TextMessageDto;
import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    private WeMessagePushClient weMessagePushClient;
    @Autowired
    private IWeSensitiveActHitService weSensitiveActHitService;
    @Autowired
    private IWeUserService weUserService;

    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        try {
            if (message.getUserId() != null && message.getExternalUserId() != null) {

                if(weCustomerService.remove(new LambdaQueryWrapper<WeCustomer>()
                        .eq(WeCustomer::getExternalUserid,message.getExternalUserId())
                        .eq(WeCustomer::getFirstUserId,message.getUserId()))){

                    WeCorpAccount validWeCorpAccount = weCorpAccountService.findValidWeCorpAccount();
                    Optional.ofNullable(validWeCorpAccount).ifPresent(weCorpAccount -> {
                        String customerChurnNoticeSwitch = weCorpAccount.getCustomerChurnNoticeSwitch();
                        if (WeConstans.DEL_FOLLOW_USER_SWITCH_OPEN.equals(customerChurnNoticeSwitch)) {

                            List<WeCustomerList> weCustomerList = weCustomerService.findWeCustomerList(WeCustomerList.builder()
                                    .externalUserid(message.getExternalUserId())
                                    .firstUserId(message.getUserId())
                                    .delFlag(new Integer(1))
                                    .build());


                            if(CollectionUtil.isNotEmpty(weCustomerList)){

                                WeCustomerList weCustomer = weCustomerList.stream().findFirst().get();

                                String content = "您已经被客户@" + weCustomer.getCustomerName() + "删除!";
                                TextMessageDto textMessageDto = new TextMessageDto();
                                textMessageDto.setContent(content);
                                WeMessagePushDto weMessagePushDto = new WeMessagePushDto();
                                weMessagePushDto.setMsgtype(MessageType.TEXT.getMessageType());
                                weMessagePushDto.setTouser(message.getUserId());
                                weMessagePushDto.setText(textMessageDto);

                                Optional.ofNullable(validWeCorpAccount).map(WeCorpAccount::getAgentId).ifPresent(agentId -> {
                                    weMessagePushDto.setAgentid(Integer.valueOf(agentId));
                                });

                                weMessagePushClient.sendMessageToUser(weMessagePushDto,weMessagePushDto.getAgentid().toString());

                                //增加敏感行为记录，客户删除员工
                                WeSensitiveAct weSensitiveAct = weSensitiveActHitService.getSensitiveActType("拉黑/删除好友");
                                if (weSensitiveAct != null && weSensitiveAct.getEnableFlag() == 1) {
                                    WeSensitiveActHit weSensitiveActHit = new WeSensitiveActHit();
                                    weSensitiveActHit.setSensitiveActId(weSensitiveAct.getId());
                                    weSensitiveActHit.setSensitiveAct(weSensitiveAct.getActName());
                                    weSensitiveActHit.setCreateTime(new Date(message.getCreateTime()));
                                    weSensitiveActHit.setCreateBy("admin");
                                    WeUser user = weUserService.getById(message.getUserId());
                                    weSensitiveActHit.setOperatorId(weCustomer.getFirstUserId());
                                    weSensitiveActHit.setOperator(weCustomer.getCustomerName());
                                    weSensitiveActHit.setOperateTargetId(user.getUserId());
                                    weSensitiveActHit.setOperateTarget(user.getName());
                                    weSensitiveActHitService.insertWeSensitiveActHit(weSensitiveActHit);
                                }



                            }




                        }
                    });

                }
            }
        } catch (Exception e) {
            log.error("删除跟进成员事件>>>>>>>>>>>>>param:{},ex:{}", JSONObject.toJSONString(message), e);
        }
    }
}
