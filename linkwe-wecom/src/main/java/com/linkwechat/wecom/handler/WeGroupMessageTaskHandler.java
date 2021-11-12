package com.linkwechat.wecom.handler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeCustomerMessagePushClient;
import com.linkwechat.wecom.domain.WeGroupMessageList;
import com.linkwechat.wecom.domain.WeGroupMessageTemplate;
import com.linkwechat.wecom.domain.WeMessageTemplate;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import com.linkwechat.wecom.domain.dto.message.SendMessageResultDto;
import com.linkwechat.wecom.domain.query.WeAddGroupMessageQuery;
import com.linkwechat.wecom.domain.query.WeAddMsgTemplateQuery;
import com.linkwechat.wecom.service.IWeGroupMessageListService;
import com.linkwechat.wecom.service.IWeGroupMessageTemplateService;
import com.linkwechat.wecom.service.IWeMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author danmo
 * @description 群发任务发送执行器
 * @date 2021/10/29 11:43
 **/
@Slf4j
@Component
public class WeGroupMessageTaskHandler implements ApplicationRunner {

    @Autowired
    private IWeGroupMessageListService groupMessageListService;

    @Autowired
    private IWeGroupMessageTemplateService groupMessageTemplateService;

    @Autowired
    private WeCustomerMessagePushClient customerMessagePushClient;

    @Autowired
    private IWeMaterialService weMaterialService;

    @Autowired
    private RedisCache redisCache;

    private AtomicLong templateId;

    @Override
    public void run(ApplicationArguments args) {
        ThreadUtil.execute(this::groupMessageTaskHandler);
    }


    public void groupMessageTaskHandler() {
        while (WeConstans.WEGROUPMSGTIMEDTASK_SWITCH) {
            try {
                Set<ZSetOperations.TypedTuple<String>> typedTuples = redisCache.rangeWithScore(WeConstans.WEGROUPMSGTIMEDTASK_KEY, 0, 0);
                Optional.ofNullable(typedTuples).orElseGet(HashSet::new).forEach(item -> {
                    long score = Objects.requireNonNull(item.getScore()).longValue();
                    if (score < System.currentTimeMillis()) {
                        try {
                            //当时间小于当前时间时删除第一个记录，并发送群发消息
                            log.info("获取到消息,执行群发任务: {}", item.getValue());
                            WeAddGroupMessageQuery query = JSONObject.parseObject(item.getValue(), WeAddGroupMessageQuery.class);
                            if (query != null) {
                                templateId = new AtomicLong(query.getId());
                                Optional.of(query).map(WeAddGroupMessageQuery::getSenderList).orElseGet(ArrayList::new).forEach(sender -> {
                                    WeAddMsgTemplateQuery templateQuery = new WeAddMsgTemplateQuery();
                                    templateQuery.setChat_type(query.getChatType());
                                    templateQuery.setSender(sender.getUserId());
                                    if (ObjectUtil.equal(1, query.getChatType())) {
                                        templateQuery.setExternal_userid(sender.getCustomerList());
                                    }
                                    getMediaId(query.getAttachmentsList());
                                    templateQuery.setAttachments(query.getAttachmentsList());
                                    if (StringUtils.isNotEmpty(query.getContent())) {
                                        WeAddMsgTemplateQuery.Text text = new WeAddMsgTemplateQuery.Text();
                                        text.setContent(query.getContent());
                                        templateQuery.setText(text);
                                    }
                                    SendMessageResultDto resultDto = customerMessagePushClient.addMsgTemplate(templateQuery);
                                    if (resultDto != null && ObjectUtil.equal(WeConstans.WE_SUCCESS_CODE, resultDto.getErrcode())) {
                                        String msgid = resultDto.getMsgid();
                                        Long msgTemplateId = query.getId();
                                        WeGroupMessageList messageList = new WeGroupMessageList();
                                        messageList.setMsgId(msgid);
                                        groupMessageListService.update(messageList, new LambdaQueryWrapper<WeGroupMessageList>()
                                                .eq(WeGroupMessageList::getMsgTemplateId, msgTemplateId)
                                                .eq(WeGroupMessageList::getUserId, sender.getUserId()));
                                    }
                                });
                            }
                        } finally {
                            redisCache.removeRangeCacheZSet(WeConstans.WEGROUPMSGTIMEDTASK_KEY, 0, 0);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                //任务异常修改模板状态为失败
                if (templateId != null) {
                    WeGroupMessageTemplate template = new WeGroupMessageTemplate();
                    template.setId(templateId.get());
                    template.setStatus(-1);
                    groupMessageTemplateService.updateById(template);
                }
            }
        }
    }

    void getMediaId(List<WeMessageTemplate> messageTemplates){
        Optional.ofNullable(messageTemplates).orElseGet(ArrayList::new).forEach(messageTemplate -> {
            if (ObjectUtil.equal(MessageType.IMAGE.getMessageType(), messageTemplate.getMsgType())) {
                WeMediaDto weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplate.getPicUrl()
                        ,MessageType.IMAGE.getMessageType()
                        ,FileUtil.getName(messageTemplate.getPicUrl()));
                messageTemplate.setMediaId(weMedia.getMedia_id());
            }else if (ObjectUtil.equal(MessageType.MINIPROGRAM.getMessageType(), messageTemplate.getMsgType())) {
                WeMediaDto weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplate.getMediaId()
                        ,MessageType.IMAGE.getMessageType()
                        ,FileUtil.getName(messageTemplate.getMediaId()));
                messageTemplate.setMediaId(weMedia.getMedia_id());
            } else if (ObjectUtil.equal(MessageType.VIDEO.getMessageType(), messageTemplate.getMsgType())) {
                WeMediaDto weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplate.getMediaId()
                        ,MessageType.IMAGE.getMessageType()
                        ,FileUtil.getName(messageTemplate.getMediaId()));
                messageTemplate.setMediaId(weMedia.getMedia_id());
            } else if (ObjectUtil.equal(MessageType.FILE.getMessageType(), messageTemplate.getMsgType())) {
                WeMediaDto weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplate.getMediaId()
                        ,MessageType.IMAGE.getMessageType()
                        ,FileUtil.getName(messageTemplate.getMediaId()));
                messageTemplate.setMediaId(weMedia.getMedia_id());
            }
        });
    }
}
