package com.linkwechat.service.impl.strategic.shortlink;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.*;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionTemplateClientAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionTemplateClientUpdateQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionUpdateQuery;
import com.linkwechat.mapper.WeShortLinkPromotionMapper;
import com.linkwechat.mapper.WeShortLinkPromotionTemplateClientMapper;
import com.linkwechat.service.IWeShortLinkPromotionAttachmentService;
import com.linkwechat.service.IWeShortLinkPromotionSendResultService;
import com.linkwechat.service.IWeShortLinkUserPromotionTaskService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 短链推广方式-客户
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/08 16:44
 */
@Service
public class ClientPromotion extends PromotionType {

    @Resource
    private WeShortLinkPromotionMapper weShortLinkPromotionMapper;
    @Resource
    private WeShortLinkPromotionTemplateClientMapper weShortLinkPromotionTemplateClientMapper;
    @Resource
    private IWeShortLinkPromotionAttachmentService weShortLinkPromotionAttachmentService;
    @Resource
    private IWeShortLinkPromotionSendResultService weShortLinkPromotionSendResultService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private RabbitMQSettingConfig rabbitMQSettingConfig;
    @Resource
    private IWeShortLinkUserPromotionTaskService weShortLinkUserPromotionTaskService;

    @Override
    public Long saveAndSend(WeShortLinkPromotionAddQuery query, WeShortLinkPromotion weShortLinkPromotion) {

        WeShortLinkPromotionTemplateClientAddQuery clientAddQuery = query.getClient();

        //发送类型：0立即发送 1定时发送
        Integer sendType = clientAddQuery.getSendType();
        if (sendType.equals(0)) {
            //任务状态: 0待推广 1推广中 2已结束
            weShortLinkPromotion.setTaskStatus(1);
            weShortLinkPromotion.setTaskStartTime(LocalDateTime.now());
        } else {
            weShortLinkPromotion.setTaskStatus(0);
            weShortLinkPromotion.setTaskStartTime(clientAddQuery.getTaskSendTime());
        }

        //任务结束时间
        LocalDateTime taskEndTime = clientAddQuery.getTaskEndTime();
        Optional.ofNullable(taskEndTime).ifPresent(o -> {
            weShortLinkPromotion.setTaskEndTime(o);
        });
        //保存短链推广
        weShortLinkPromotionMapper.insert(weShortLinkPromotion);
        //保存短链推广模板-客户
        WeShortLinkPromotionTemplateClient client = BeanUtil.copyProperties(clientAddQuery, WeShortLinkPromotionTemplateClient.class);
        client.setPromotionId(weShortLinkPromotion.getId());
        client.setDelFlag(0);
        weShortLinkPromotionTemplateClientMapper.insert(client);

        //保存附件
        Optional.ofNullable(query.getAttachmentsList()).ifPresent(attachments -> {
            List<WeShortLinkPromotionAttachment> collect = attachments.stream().map(attachment -> {
                WeShortLinkPromotionAttachment weShortLinkPromotionAttachment = BeanUtil.copyProperties(attachment, WeShortLinkPromotionAttachment.class);
                //附件所属类型：0群发客户 1群发客群 2朋友圈
                weShortLinkPromotionAttachment.setTemplateType(0);
                weShortLinkPromotionAttachment.setTemplateId(client.getId());
                weShortLinkPromotionAttachment.setDelFlag(0);
                return weShortLinkPromotionAttachment;
            }).collect(Collectors.toList());
            weShortLinkPromotionAttachmentService.saveBatch(collect);
        });

        //保存员工短链推广任务
        List<WeAddGroupMessageQuery.SenderInfo> senderList = query.getSenderList();
        List<WeShortLinkUserPromotionTask> weShortLinkUserPromotionTasks = new ArrayList<>();
        for (WeAddGroupMessageQuery.SenderInfo senderInfo : senderList) {
            WeShortLinkUserPromotionTask weShortLinkUserPromotionTask = new WeShortLinkUserPromotionTask();
            weShortLinkUserPromotionTask.setUserId(senderInfo.getUserId());
            weShortLinkUserPromotionTask.setTemplateType(0);
            weShortLinkUserPromotionTask.setTemplateId(client.getId());
            weShortLinkUserPromotionTask.setSendStatus(0);
            weShortLinkUserPromotionTask.setAllClientNum(senderInfo.getCustomerList().size());
            weShortLinkUserPromotionTask.setRealClientNum(0);
            weShortLinkUserPromotionTask.setDelFlag(0);
            weShortLinkUserPromotionTasks.add(weShortLinkUserPromotionTask);
        }
        weShortLinkUserPromotionTaskService.saveBatch(weShortLinkUserPromotionTasks);

        //保存发送人员
        Optional.ofNullable(query.getSenderList()).ifPresent(senderInfos -> {
            List<WeShortLinkPromotionSendResult> sendResultList = new ArrayList<>();
            senderInfos.stream().forEach(senderInfo -> {
                List<String> customerList = senderInfo.getCustomerList();
                for (String externalUserid : customerList) {
                    WeShortLinkPromotionSendResult weShortLinkPromotionSendResult = new WeShortLinkPromotionSendResult();
                    weShortLinkPromotionSendResult.setTemplateType(0);
                    weShortLinkPromotionSendResult.setTemplateId(client.getId());
                    weShortLinkPromotionSendResult.setUserId(senderInfo.getUserId());
                    weShortLinkPromotionSendResult.setExternalUserid(externalUserid);
                    weShortLinkPromotionSendResult.setStatus(0);
                    weShortLinkPromotionSendResult.setStatus(0);
                    weShortLinkPromotionSendResult.setDelFlag(0);
                    sendResultList.add(weShortLinkPromotionSendResult);
                }
            });
            weShortLinkPromotionSendResultService.saveBatch(sendResultList);
        });

        //mq执行推送消息给员工
        if (sendType.equals(0)) {
            //任务状态: 0待推广 1推广中 2已结束
            directSend(weShortLinkPromotion.getId(), client.getId(), clientAddQuery.getContent(), query.getAttachmentsList(), query.getSenderList());
        } else {
            //定时发送
            timingSend(weShortLinkPromotion.getId(), client.getId(), clientAddQuery.getContent(), Date.from(query.getClient().getTaskSendTime().atZone(ZoneId.systemDefault()).toInstant()), query.getAttachmentsList(), query.getSenderList());
        }
        //任务结束时间
        Optional.ofNullable(taskEndTime).ifPresent(o -> {
            //TODO mq 定时结束
            timingEnd();
        });


        return weShortLinkPromotion.getId();
    }

    @Override
    public void updateAndSend(WeShortLinkPromotionUpdateQuery query, WeShortLinkPromotion weShortLinkPromotion) {

        WeShortLinkPromotionTemplateClientUpdateQuery clientUpdateQuery = query.getClient();

        //发送类型：0立即发送 1定时发送
        Integer sendType = clientUpdateQuery.getSendType();
        if (sendType.equals(0)) {
            //任务状态: 0待推广 1推广中 2已结束 3暂存中
            weShortLinkPromotion.setTaskStatus(1);
            weShortLinkPromotion.setTaskStartTime(LocalDateTime.now());
        } else {
            weShortLinkPromotion.setTaskStatus(0);
            weShortLinkPromotion.setTaskStartTime(clientUpdateQuery.getTaskSendTime());
        }
        //任务结束时间
        LocalDateTime taskEndTime = clientUpdateQuery.getTaskEndTime();
        Optional.ofNullable(taskEndTime).ifPresent(o -> weShortLinkPromotion.setTaskEndTime(o));
        //保存短链推广
        weShortLinkPromotionMapper.updateById(weShortLinkPromotion);

        //删除短链推广模板-客户
        WeShortLinkPromotionTemplateClient weShortLinkPromotionTemplateClient = new WeShortLinkPromotionTemplateClient();
        weShortLinkPromotionTemplateClient.setDelFlag(1);
        LambdaUpdateWrapper<WeShortLinkPromotionTemplateClient> clientUpdateWrapper = Wrappers.lambdaUpdate();
        clientUpdateWrapper.eq(WeShortLinkPromotionTemplateClient::getId, clientUpdateQuery.getId());
        weShortLinkPromotionTemplateClientMapper.update(weShortLinkPromotionTemplateClient, clientUpdateWrapper);
        //保存短链推广模板-客户
        WeShortLinkPromotionTemplateClient client = BeanUtil.copyProperties(clientUpdateQuery, WeShortLinkPromotionTemplateClient.class);
        client.setId(null);
        client.setPromotionId(weShortLinkPromotion.getId());
        client.setDelFlag(0);
        weShortLinkPromotionTemplateClientMapper.insert(client);

        //删除短链推广模板附件
        LambdaUpdateWrapper<WeShortLinkPromotionAttachment> attachmentUpdateWrapper = Wrappers.lambdaUpdate();
        attachmentUpdateWrapper.eq(WeShortLinkPromotionAttachment::getTemplateId, client.getId());
        attachmentUpdateWrapper.eq(WeShortLinkPromotionAttachment::getTemplateType, 0);
        attachmentUpdateWrapper.set(WeShortLinkPromotionAttachment::getDelFlag, 1);
        weShortLinkPromotionAttachmentService.update(attachmentUpdateWrapper);
        //保存附件
        Optional.ofNullable(query.getAttachmentsList()).ifPresent(attachments -> {
            List<WeShortLinkPromotionAttachment> collect = attachments.stream().map(attachment -> {
                WeShortLinkPromotionAttachment weShortLinkPromotionAttachment = BeanUtil.copyProperties(attachment, WeShortLinkPromotionAttachment.class);
                //附件所属类型：0群发客户 1群发客群 2朋友圈
                weShortLinkPromotionAttachment.setTemplateType(0);
                weShortLinkPromotionAttachment.setTemplateId(client.getId());
                return weShortLinkPromotionAttachment;
            }).collect(Collectors.toList());
            weShortLinkPromotionAttachmentService.saveBatch(collect);
        });

        //删除员工短链推广任务
        LambdaUpdateWrapper<WeShortLinkUserPromotionTask> userPromotionTaskUpdateWrapper = Wrappers.lambdaUpdate();
        userPromotionTaskUpdateWrapper.eq(WeShortLinkUserPromotionTask::getTemplateType, 0);
        userPromotionTaskUpdateWrapper.eq(WeShortLinkUserPromotionTask::getTemplateId, clientUpdateQuery.getId());
        userPromotionTaskUpdateWrapper.set(WeShortLinkUserPromotionTask::getDelFlag, 1);
        weShortLinkUserPromotionTaskService.update(userPromotionTaskUpdateWrapper);
        //保存员工短链推广任务
        List<WeAddGroupMessageQuery.SenderInfo> senderList = query.getSenderList();
        List<WeShortLinkUserPromotionTask> weShortLinkUserPromotionTasks = new ArrayList<>();
        for (WeAddGroupMessageQuery.SenderInfo senderInfo : senderList) {
            WeShortLinkUserPromotionTask weShortLinkUserPromotionTask = new WeShortLinkUserPromotionTask();
            weShortLinkUserPromotionTask.setUserId(senderInfo.getUserId());
            weShortLinkUserPromotionTask.setTemplateType(0);
            weShortLinkUserPromotionTask.setTemplateId(client.getId());
            weShortLinkUserPromotionTask.setSendStatus(0);
            weShortLinkUserPromotionTask.setAllClientNum(senderInfo.getCustomerList().size());
            weShortLinkUserPromotionTask.setRealClientNum(0);
            weShortLinkUserPromotionTask.setDelFlag(0);
            weShortLinkUserPromotionTasks.add(weShortLinkUserPromotionTask);
        }
        weShortLinkUserPromotionTaskService.saveBatch(weShortLinkUserPromotionTasks);

        List<Long> businessIds = weShortLinkUserPromotionTasks.stream().map(i -> i.getId()).collect(Collectors.toList());

        //删除短链推广发送结果
        LambdaUpdateWrapper<WeShortLinkPromotionSendResult> promotionSendResultUpdateWrapper = Wrappers.lambdaUpdate();
        promotionSendResultUpdateWrapper.eq(WeShortLinkPromotionSendResult::getTemplateType, 0);
        promotionSendResultUpdateWrapper.eq(WeShortLinkPromotionSendResult::getTemplateId, client.getId());
        promotionSendResultUpdateWrapper.set(WeShortLinkPromotionSendResult::getDelFlag, 1);
        weShortLinkPromotionSendResultService.update(promotionSendResultUpdateWrapper);
        //保存短链推广发送结果
        Optional.ofNullable(query.getSenderList()).ifPresent(senderInfos -> {
            List<WeShortLinkPromotionSendResult> sendResultList = new ArrayList<>();
            senderInfos.stream().forEach(senderInfo -> {
                List<String> customerList = senderInfo.getCustomerList();
                for (String externalUserid : customerList) {
                    WeShortLinkPromotionSendResult weShortLinkPromotionSendResult = new WeShortLinkPromotionSendResult();
                    weShortLinkPromotionSendResult.setTemplateType(0);
                    weShortLinkPromotionSendResult.setTemplateId(client.getId());
                    weShortLinkPromotionSendResult.setUserId(senderInfo.getUserId());
                    weShortLinkPromotionSendResult.setExternalUserid(externalUserid);
                    weShortLinkPromotionSendResult.setStatus(0);
                    weShortLinkPromotionSendResult.setDelFlag(0);
                    sendResultList.add(weShortLinkPromotionSendResult);
                }
            });
            weShortLinkPromotionSendResultService.saveBatch(sendResultList);
        });

        //mq执行推送消息给员工
        if (sendType.equals(0)) {
            //任务状态: 0待推广 1推广中 2已结束
            directSend(weShortLinkPromotion.getId(), client.getId(), clientUpdateQuery.getContent(), query.getAttachmentsList(), query.getSenderList());
        } else {
            //定时发送
            timingSend(weShortLinkPromotion.getId(), client.getId(), clientUpdateQuery.getContent(), Date.from(query.getClient().getTaskSendTime().atZone(ZoneId.systemDefault()).toInstant()), query.getAttachmentsList(), query.getSenderList());
        }
        //任务结束时间
        Optional.ofNullable(taskEndTime).ifPresent(o -> {
            //TODO mq 定时结束
            timingEnd();
        });


    }

    @Override
    protected void directSend(Long id, Long businessId, String content, List<WeMessageTemplate> attachments, List<WeAddGroupMessageQuery.SenderInfo> senderList) {
        WeAddGroupMessageQuery weAddGroupMessageQuery = new WeAddGroupMessageQuery();
        weAddGroupMessageQuery.setId(id);
        weAddGroupMessageQuery.setChatType(1);
        weAddGroupMessageQuery.setContent(content);
        //是否定时任务 0 立即发送 1 定时发送
        weAddGroupMessageQuery.setIsTask(0);
        weAddGroupMessageQuery.setMsgSource(4);
        weAddGroupMessageQuery.setAttachmentsList(attachments);
        weAddGroupMessageQuery.setSenderList(senderList);
        weAddGroupMessageQuery.setBusinessId(businessId);
        weAddGroupMessageQuery.setCurrentUserInfo(SecurityUtils.getLoginUser());
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeGroupMsgRk(), JSONObject.toJSONString(weAddGroupMessageQuery));
    }


    @Override
    protected void timingSend(Long id, Long businessId, String content, Date sendTime, List<WeMessageTemplate> attachments, List<WeAddGroupMessageQuery.SenderInfo> senderList) {
        WeAddGroupMessageQuery weAddGroupMessageQuery = new WeAddGroupMessageQuery();
        weAddGroupMessageQuery.setId(id);
        weAddGroupMessageQuery.setChatType(1);
        weAddGroupMessageQuery.setContent(content);
        //是否定时任务 0 立即发送 1 定时发送
        weAddGroupMessageQuery.setIsTask(1);
        weAddGroupMessageQuery.setSendTime(sendTime);
        weAddGroupMessageQuery.setMsgSource(4);
        weAddGroupMessageQuery.setAttachmentsList(attachments);
        weAddGroupMessageQuery.setSenderList(senderList);
        weAddGroupMessageQuery.setBusinessId(businessId);
        weAddGroupMessageQuery.setCurrentUserInfo(SecurityUtils.getLoginUser());

        long diffTime = DateUtils.diffTime(sendTime, new Date());
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeDelayGroupMsgRk(), JSONObject.toJSONString(weAddGroupMessageQuery), message -> {
            //注意这里时间可使用long类型,毫秒单位，设置header
            message.getMessageProperties().setHeader("x-delay", diffTime);
            return message;
        });
    }


    @Override
    protected void timingEnd() {

    }


}
