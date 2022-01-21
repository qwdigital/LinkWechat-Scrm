package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Joiner;
import com.linkwechat.common.core.domain.entity.WeCorpAccount;
import com.linkwechat.common.enums.CommunityTaskType;
import com.linkwechat.common.enums.PushType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeMessagePushClient;
import com.linkwechat.wecom.domain.WeMessagePush;
import com.linkwechat.wecom.domain.dto.WeMessagePushDto;
import com.linkwechat.wecom.domain.dto.message.TextMessageDto;
import com.linkwechat.wecom.mapper.WeMessagePushMapper;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import com.linkwechat.wecom.service.IWeMessagePushService;
import com.linkwechat.wecom.strategy.MessageContext;
import com.linkwechat.wecom.strategy.SendMessageToUserGroupStrategy;
import com.linkwechat.wecom.strategy.SendMessageToUserStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.*;

/**
 * 消息发送的Service接口
 *
 * @author KeWen
 * @date 2020-10-28
 */
@Service
public class WeMessagePushServiceImpl implements IWeMessagePushService {

    @Autowired
    private WeMessagePushMapper weMessagePushMapper;

    @Autowired
    private SendMessageToUserGroupStrategy sendMessageToUserGroupStrategy;

    @Autowired
    private SendMessageToUserStrategy sendMessageToUserStrategy;

    @Autowired
    private IWeCorpAccountService corpAccountService;

    @Autowired
    private WeMessagePushClient messagePushClient;

    @Override
    public WeMessagePush selectWeMessagePushById(Long messagePushId) {
        return weMessagePushMapper.selectById(messagePushId);
    }

    @Override
    public List<WeMessagePush> selectWeMessagePushList(WeMessagePush weMessagePush) {
        return weMessagePushMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public void insertWeMessagePush(WeMessagePush weMessagePush) {

        sendMessgae(weMessagePush);

    }

    /**
     * 发送消息
     *
     * @param weMessagePush
     */
    public void sendMessgae(WeMessagePush weMessagePush) {
        if (weMessagePush.getPushType() != null && weMessagePush.getPushType().equals(PushType.SEND_TO_USER.getType())) {
            new MessageContext(sendMessageToUserStrategy).sendMessage(weMessagePush);
        }
        if (weMessagePush.getPushType() != null
                && weMessagePush.getPushType().equals(PushType.SENT_TO_USER_GROUP.getType())) {
            new MessageContext(sendMessageToUserGroupStrategy).sendMessage(weMessagePush);
        }
    }

    @Override
    public int deleteWeMessagePushByIds(Long[] messagePushIds) {
        return weMessagePushMapper.deleteBatchIds(Arrays.asList(messagePushIds));
    }

    @Override
    public int deleteWeMessagePushById(Long messagePushId) {
        return weMessagePushMapper.deleteById(messagePushId);
    }

    @Override
    public void pushMessageSelfH5(List<String> toUser,String textContent,Integer taskType) {

        if(CollectionUtil.isEmpty(toUser)){
            throw new WeComException("接受人不可为空");
        }

        WeMessagePushDto pushDto = new WeMessagePushDto();

        // 获取agentId
        WeCorpAccount validWeCorpAccount = corpAccountService.findValidWeCorpAccount();
        String agentId = validWeCorpAccount.getAgentId();
        String corpId = validWeCorpAccount.getCorpId();
        if (StringUtils.isEmpty(agentId)) {
            throw new WeComException("当前agentId不可用或不存在");
        }

        if(StringUtils.isEmpty(validWeCorpAccount.getAuthorizeUrl())){
            throw new WeComException("JS SDK 身份校验url不可为空");
        }

        pushDto.setAgentid(Integer.valueOf(agentId));
        //推送给指定员工
        pushDto.setTouser(Joiner.on("|").join(toUser));
        // 设置消息内容
        pushDto.setMsgtype("text");
        TextMessageDto text = new TextMessageDto();
        String REDIRECT_URI = null;

        if(taskType.equals(CommunityTaskType.SEAS.getType())){ //客户公海链接
            if(StringUtils.isEmpty( validWeCorpAccount.getSeasRedirectUrl())){
                throw new WeComException("客户公海H5跳转链接不可为空");
            }
            REDIRECT_URI=URLEncoder.encode(String.format("%s?corpId=%s&agentId=%s",
                            validWeCorpAccount.getSeasRedirectUrl()
                    , corpId, agentId));
        }else{//群sop与老客标签建群
            if(StringUtils.isEmpty( validWeCorpAccount.getSopTagRedirectUrl())){
                throw new WeComException("群SOP与老客标签建群H5跳转链接不可为空");
            }
            REDIRECT_URI = URLEncoder.encode(String.format("%s?corpId=%s&agentId=%s&type=%s", validWeCorpAccount.getSopTagRedirectUrl(), corpId, agentId,taskType));

        }

        if(StringUtils.isNotEmpty(REDIRECT_URI)){
            String context = String.format(
                    textContent+"<br><a href='%s?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect'>请点击此链接查看</a>",
                    validWeCorpAccount.getAuthorizeUrl(), corpId, REDIRECT_URI);
            text.setContent(context);
            pushDto.setText(text);
            // 请求消息推送接口，获取结果 [消息推送 - 发送应用消息]
            messagePushClient.sendMessageToUser(pushDto, agentId);
        }



    }

}
