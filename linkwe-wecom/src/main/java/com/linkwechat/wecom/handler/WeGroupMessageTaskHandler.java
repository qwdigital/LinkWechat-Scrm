package com.linkwechat.wecom.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.common.enums.MessageType;
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
import com.linkwechat.wecom.service.event.WeEventPublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

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
    private WeEventPublisherService weEventPublisherService;

    @Autowired
    private RedisCache redisCache;

    private WeAddGroupMessageQuery query;


    @Override
    public void run(ApplicationArguments args) {
        ThreadUtil.execute(this::groupMessageTaskHandler);
    }


    public void groupMessageTaskHandler() {
        while (WeConstans.WEGROUPMSGTIMEDTASK_SWITCH) {
            Set<ZSetOperations.TypedTuple<String>> typedTuples = redisCache.rangeWithScore(WeConstans.WEGROUPMSGTIMEDTASK_KEY, 0, 0);
            if (CollectionUtil.isEmpty(typedTuples)) {
                try {
                    TimeUnit.MILLISECONDS.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            typedTuples.forEach(item -> {
                long score = Objects.requireNonNull(item.getScore()).longValue();
                if (score < System.currentTimeMillis()) {
                    try {
                        //当时间小于当前时间时删除第一个记录，并发送群发消息
                        log.info("获取到消息,执行群发任务: {}", item.getValue());
                        query = JSONObject.parseObject(item.getValue(), WeAddGroupMessageQuery.class);
                        if (query != null) {
                            Optional.of(query).map(WeAddGroupMessageQuery::getSenderList).orElseGet(ArrayList::new).forEach(sender -> {
                                WeAddMsgTemplateQuery templateQuery = new WeAddMsgTemplateQuery();
                                templateQuery.setChat_type(query.getChatType());
                                templateQuery.setSender(sender.getUserId());
                                if (ObjectUtil.equal(1, query.getChatType())) {
                                    templateQuery.setExternal_userid(sender.getCustomerList());
                                }
                                getMediaId(query.getAttachmentsList());
                                templateQuery.setAttachments(query.getAttachmentsList());
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
                            WeGroupMessageTemplate template = new WeGroupMessageTemplate();
                            template.setId(query.getId());
                            template.setStatus(1);
                            groupMessageTemplateService.updateById(template);
                            if (!ObjectUtil.equal(0, query.getSource())) {
                                weEventPublisherService.callBackTask(query.getBusinessId(), query.getSource(), 1);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //任务异常修改模板状态为失败
                        if (query.getId() != null) {
                            WeGroupMessageTemplate template = new WeGroupMessageTemplate();
                            template.setId(query.getId());
                            template.setStatus(-1);
                            groupMessageTemplateService.updateById(template);
                        }
                        if (!ObjectUtil.equal(0, query.getSource())) {
                            weEventPublisherService.callBackTask(query.getBusinessId(), query.getSource(), -1);
                        }
                    } finally {
                        redisCache.removeRangeCacheZSet(WeConstans.WEGROUPMSGTIMEDTASK_KEY, 0, 0);
                    }
                }
            });
        }
    }

    void getMediaId(List<WeMessageTemplate> messageTemplates) {
        try {
            Optional.ofNullable(messageTemplates).orElseGet(ArrayList::new).forEach(messageTemplate -> {
                if (ObjectUtil.equal(MessageType.IMAGE.getMessageType(), messageTemplate.getMsgType())) {
                    WeMediaDto weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplate.getMediaId()
                            , MessageType.IMAGE.getMessageType()
                            , FileUtil.getName(messageTemplate.getMediaId()));
                    messageTemplate.setMediaId(weMedia.getMedia_id());
                } else if (ObjectUtil.equal(MessageType.MINIPROGRAM.getMessageType(), messageTemplate.getMsgType())) {
                    WeMediaDto weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplate.getMediaId()
                            , MessageType.IMAGE.getMessageType()
                            , FileUtil.getName(messageTemplate.getMediaId()));
                    messageTemplate.setMediaId(weMedia.getMedia_id());
                } else if (ObjectUtil.equal(MessageType.VIDEO.getMessageType(), messageTemplate.getMsgType())) {
                    WeMediaDto weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplate.getMediaId()
                            , MessageType.IMAGE.getMessageType()
                            , FileUtil.getName(messageTemplate.getMediaId()));
                    messageTemplate.setMediaId(weMedia.getMedia_id());
                } else if (ObjectUtil.equal(MessageType.FILE.getMessageType(), messageTemplate.getMsgType())) {
                    WeMediaDto weMedia = weMaterialService.uploadTemporaryMaterial(messageTemplate.getMediaId()
                            , MessageType.IMAGE.getMessageType()
                            , FileUtil.getName(messageTemplate.getMediaId()));
                    messageTemplate.setMediaId(weMedia.getMedia_id());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
