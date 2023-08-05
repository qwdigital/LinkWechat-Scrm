package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.enums.MessageNoticeType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.msg.QwAppMsgBody;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.service.IWeMessagePushService;
import com.linkwechat.service.QwAppSendMsgService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

@Service
public class WeMessagePushServiceImpl implements IWeMessagePushService {


    @Autowired
    private QwAppSendMsgService qwAppSendMsgService;

    @Autowired
    private IWeCorpAccountService iWeCorpAccountService;


    @Autowired
    private LinkWeChatConfig linkWeChatConfig;


    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;





    @Override
    public void pushMessageSelfH5(List<String> toUser, String textContent, Integer taskType,boolean isJumpUrl) {
        if(CollectionUtil.isEmpty(toUser)){
            throw new WeComException("接受人不可为空");
        }


        if(StringUtils.isEmpty(linkWeChatConfig.getAuthorizeUrl())){
            throw new WeComException("JS SDK 身份校验url不可为空");
        }


        WeCorpAccount weCorpAccount = iWeCorpAccountService.getCorpAccountByCorpId(null);



        if(null != weCorpAccount){
            if(StringUtils.isEmpty(weCorpAccount.getAgentId())){
                throw new WeComException("应用相关配置不可为空");
            }

            QwAppMsgBody  qwAppMsgBody=new QwAppMsgBody();
            //指定企业
            qwAppMsgBody.setCorpId(weCorpAccount.getCorpId());
            //发送人指定员工
            qwAppMsgBody.setCorpUserIds(toUser);

            //设置消息模板
            WeMessageTemplate template=new WeMessageTemplate();
            //设置消息内型
            template.setMsgType("text");
            //设置应用id
            template.setAppId(weCorpAccount.getAgentId());

            if(isJumpUrl){ //需要跳转链接的通知
                String REDIRECT_URI = null;

                if(taskType.equals(MessageNoticeType.SEAS.getType())){ //客户公海链接
                    if(StringUtils.isEmpty( linkWeChatConfig.getSeasRedirectUrl())){
                        throw new WeComException("客户公海H5跳转链接不可为空");
                    }
                    REDIRECT_URI= URLEncoder.encode(String.format("%s?corpId=%s&agentId=%s",
                            linkWeChatConfig.getSeasRedirectUrl()
                            , weCorpAccount.getCorpId(), weCorpAccount.getAgentId()));
//                    REDIRECT_URI= linkWeChatConfig.getSeasRedirectUrl();
                }else if(taskType.equals(MessageNoticeType.DELETEWEUSER.getType())){//客户删除员工
                    if(StringUtils.isEmpty( linkWeChatConfig.getLostCustomerRedirectUrl())){
                        throw new WeComException("流失客户H5跳转链接不可为空");
                    }
                    REDIRECT_URI= URLEncoder.encode(String.format("%s?corpId=%s&agentId=%s",
                            linkWeChatConfig.getLostCustomerRedirectUrl()
                            , weCorpAccount.getCorpId(), weCorpAccount.getAgentId()));

                } else if(taskType.equals(MessageNoticeType.CUSTOMER_SOP.getType())){//客户sop

                    if(StringUtils.isEmpty(linkWeChatConfig.getCustomerSopRedirectUrl())){
                        throw new WeComException("客户SOP的H5跳转链接不可为空");
                    }
                    REDIRECT_URI = URLEncoder.encode(String.format("%s?corpId=%s&agentId=%s&type=%s&isExpiringSoon=%s",
                            linkWeChatConfig.getCustomerSopRedirectUrl(), weCorpAccount.getCorpId(), weCorpAccount.getAgentId(),taskType,false));


                }else if(taskType.equals(MessageNoticeType.CUSTOMER_SOP_DQTX.getType())){ //客户sop即将到期提醒

                    if(StringUtils.isEmpty(linkWeChatConfig.getCustomerSopRedirectUrl())){
                        throw new WeComException("客户SOP的H5跳转链接不可为空");
                    }
                    REDIRECT_URI = URLEncoder.encode(String.format("%s?corpId=%s&agentId=%s&type=%s&isExpiringSoon=%s",
                            linkWeChatConfig.getCustomerSopRedirectUrl(), weCorpAccount.getCorpId(), weCorpAccount.getAgentId(),taskType,true));



                }else if(taskType.equals(MessageNoticeType.GROUP_SOP.getType())){//客群sop

                    if(StringUtils.isEmpty(linkWeChatConfig.getGroupSopRedirectUrl())){
                        throw new WeComException("客群SOP的H5跳转链接不可为空");
                    }
                    REDIRECT_URI = URLEncoder.encode(String.format("%s?corpId=%s&agentId=%s&type=%s&isExpiringSoon=%s",
                            linkWeChatConfig.getGroupSopRedirectUrl(), weCorpAccount.getCorpId(), weCorpAccount.getAgentId(),taskType,false));


                }else if(taskType.equals(MessageNoticeType.GROUP_SOP_DQTX.getType())){ //客群sop即将到期提醒


                    if(StringUtils.isEmpty(linkWeChatConfig.getGroupSopRedirectUrl())){
                        throw new WeComException("客群SOP的H5跳转链接不可为空");
                    }
                    REDIRECT_URI = URLEncoder.encode(String.format("%s?corpId=%s&agentId=%s&type=%s&isExpiringSoon=%s",
                            linkWeChatConfig.getGroupSopRedirectUrl(), weCorpAccount.getCorpId(), weCorpAccount.getAgentId(),taskType,true));


                }else{//老客标签建群
                    if(StringUtils.isEmpty( linkWeChatConfig.getTagRedirectUrl())){
                        throw new WeComException("标签建群H5跳转链接不可为空");
                    }
                    REDIRECT_URI = URLEncoder.encode(String.format("%s?corpId=%s&agentId=%s&type=%s", linkWeChatConfig.getTagRedirectUrl(),
                            weCorpAccount.getCorpId(), weCorpAccount.getAgentId(),taskType));

//                    REDIRECT_URI = linkWeChatConfig.getTagRedirectUrl();

                }

                if(StringUtils.isNotEmpty(REDIRECT_URI)){
                    String context = String.format(
                            textContent+"<br/><br/> <a href='%s?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect'>请点击此链接查看更多</a>",
                            linkWeChatConfig.getAuthorizeUrl(),  weCorpAccount.getCorpId(), REDIRECT_URI);
//                    String context = textContent+"<br/><br/> <a href='"+REDIRECT_URI+"'>请点击此链接查看</a>";
                    template.setContent(context);
                    qwAppMsgBody.setMessageTemplates(template);
                    // 请求消息推送接口，获取结果 [消息推送 - 发送应用消息]
                    qwAppSendMsgService.appMsgSend(qwAppMsgBody);
                }
            }else{//不需要跳转url
                template.setContent(textContent);
                qwAppMsgBody.setMessageTemplates(template);
                // 请求消息推送接口，获取结果 [消息推送 - 发送应用消息]
                qwAppSendMsgService.appMsgSend(qwAppMsgBody);
            }



        }






    }

    @Override
    public void officialPushMessage(WeAddGroupMessageQuery query) {
        if (ObjectUtil.equal(0, query.getIsTask()) && query.getSendTime() == null) {
            //todo 立即发送
            rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(),rabbitMQSettingConfig.getWeGroupMsgRk(), JSONObject.toJSONString(query));
        } else {
            //todo 延时发送
            long diffTime = DateUtils.diffTime(query.getSendTime(), new Date());
            if(diffTime>0){
                rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(),rabbitMQSettingConfig.getWeDelayGroupMsgRk(),JSONObject.toJSONString(query),message -> {
                    //注意这里时间可使用long类型,毫秒单位，设置header
                    message.getMessageProperties().setHeader("x-delay", diffTime);
                    return message;
                });
            }

        }

    }
}
