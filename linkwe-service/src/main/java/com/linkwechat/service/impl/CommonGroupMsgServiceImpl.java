package com.linkwechat.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.enums.QwGroupMsgBusinessTypeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.domain.WeGroupMessageList;
import com.linkwechat.domain.WeGroupMessageTemplate;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.wecom.query.customer.msg.WeAddCustomerMsgQuery;
import com.linkwechat.domain.wecom.vo.customer.msg.WeAddCustomerMsgVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


/**
 *
 * 企业微信api公共群发消息
 */
@Service("commonGroupMsgService")
@Slf4j
public class CommonGroupMsgServiceImpl extends AbstractGroupMsgSendTaskService {

    @Autowired
    IWeGroupMessageTemplateService iWeGroupMessageTemplateService;

    @Autowired
    QwCustomerClient qwCustomerClient;

    @Autowired
    IWeGroupMessageListService weGroupMessageListService;

    @Autowired
    LinkWeChatConfig linkWeChatConfig;

    @Autowired
    IWeMaterialService weMaterialService;



    @Override
    public void sendGroupMsg(WeAddGroupMessageQuery query) {

        LoginUser loginUser = query.getLoginUser();
        SecurityContextHolder.setUserName(loginUser.getUserName());
        SecurityContextHolder.setCorpId(loginUser.getCorpId());
        WeGroupMessageTemplate template = iWeGroupMessageTemplateService.getById(query.getId());
        try {
            if(template != null && template.getStatus() == 0){
                Optional.of(query).map(WeAddGroupMessageQuery::getSenderList).orElseGet(ArrayList::new).forEach(sender -> {

                    WeAddCustomerMsgVo weAddCustomerMsgVo =  sendSpecGroupMsgTemplate(query,sender);


                    if (weAddCustomerMsgVo != null && ObjectUtil.equal(WeConstans.WE_SUCCESS_CODE, weAddCustomerMsgVo.getErrCode())) {
                        String msgid = weAddCustomerMsgVo.getMsgId();
                        Long msgTemplateId = query.getId();
                        WeGroupMessageList messageList = new WeGroupMessageList();
                        messageList.setMsgId(msgid);
                        weGroupMessageListService.update(messageList, new LambdaQueryWrapper<WeGroupMessageList>()
                                .eq(WeGroupMessageList::getMsgTemplateId, msgTemplateId)
                                .eq(WeGroupMessageList::getUserId, sender.getUserId()));
                    }
                });
                template.setStatus(1);
                template.setSource(query.getSource());
                iWeGroupMessageTemplateService.updateById(template);
            }
        } catch (Exception e) {
            log.error("groupMessageTaskHandler error",e);
            template.setId(query.getId());
            template.setSource(query.getSource());
            template.setStatus(-1);
            iWeGroupMessageTemplateService.updateById(template);
            throw new WeComException("发送失败");
        }finally {

            if(template != null){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("businessId",template.getBusinessId());
                jsonObject.put("source",template.getSource());
                jsonObject.put("status",template.getStatus());
                try {
                    QwGroupMsgBusinessTypeEnum qwAppMsgBusinessTypeEnum = QwGroupMsgBusinessTypeEnum.parseEnum(template.getSource());
                    if(qwAppMsgBusinessTypeEnum != null){
                        SpringUtils.getBean(qwAppMsgBusinessTypeEnum.getBeanName(), AbstractGroupMsgService.class).callBackHandler(jsonObject);
                    }
                } catch (BeansException e) {
                    log.error("groupMessageTaskHandler callback-error",e);
                }

            }

        }

    }
}
