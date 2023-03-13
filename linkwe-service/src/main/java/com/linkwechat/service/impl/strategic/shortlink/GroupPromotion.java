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
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionTemplateGroupAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionTemplateGroupUpdateQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionUpdateQuery;
import com.linkwechat.service.*;
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
 * 短链推广方式-客群
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/10 9:41
 */
@Service
public class GroupPromotion extends PromotionType {

    @Resource
    private IWeShortLinkPromotionService weShortLinkPromotionService;
    @Resource
    private IWeShortLinkPromotionTemplateGroupService weShortLinkPromotionTemplateGroupService;
    @Resource
    private IWeShortLinkUserPromotionTaskService weShortLinkUserPromotionTaskService;
    @Resource
    private IWeShortLinkPromotionAttachmentService weShortLinkPromotionAttachmentService;
    @Resource
    private IWeShortLinkPromotionSendResultService weShortLinkPromotionSendResultService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Override
    public Long saveAndSend(WeShortLinkPromotionAddQuery query, WeShortLinkPromotion weShortLinkPromotion) {
        //1. 保存短链推广
        WeShortLinkPromotionTemplateGroupAddQuery group = query.getGroup();
        Integer sendType = group.getSendType();
        //发送类型：0立即发送 1定时发送
        if (sendType.equals(0)) {
            //任务状态: 0带推广 1推广中 2已结束
            weShortLinkPromotion.setTaskStatus(1);
            weShortLinkPromotion.setTaskStartTime(LocalDateTime.now());
        } else {
            weShortLinkPromotion.setTaskStatus(0);
            weShortLinkPromotion.setTaskStartTime(group.getTaskSendTime());
        }
        Optional.ofNullable(group.getTaskEndTime()).ifPresent(i -> weShortLinkPromotion.setTaskEndTime(i));
        weShortLinkPromotionService.save(weShortLinkPromotion);

        //2.保存短链推广模板-客群
        WeShortLinkPromotionTemplateGroup templateGroup = BeanUtil.copyProperties(group, WeShortLinkPromotionTemplateGroup.class);
        templateGroup.setPromotionId(weShortLinkPromotion.getId());
        templateGroup.setDelFlag(0);
        weShortLinkPromotionTemplateGroupService.save(templateGroup);

        //3.保存员工短链推广任务
        List<WeShortLinkUserPromotionTask> list = new ArrayList<>();
        List<WeAddGroupMessageQuery.SenderInfo> senderList = query.getSenderList();
        for (WeAddGroupMessageQuery.SenderInfo senderInfo : senderList) {
            WeShortLinkUserPromotionTask weShortLinkUserPromotionTask = new WeShortLinkUserPromotionTask();
            weShortLinkUserPromotionTask.setUserId(senderInfo.getUserId());
            //任务所属类型：0群发客户 1群发客群 2朋友圈
            weShortLinkUserPromotionTask.setTemplateType(1);
            weShortLinkUserPromotionTask.setTemplateId(templateGroup.getId());
            //员工发送状态
            weShortLinkUserPromotionTask.setSendStatus(0);
            weShortLinkUserPromotionTask.setAllGroupNum(senderInfo.getChatList().size());
            weShortLinkUserPromotionTask.setRealGroupNum(0);
            weShortLinkUserPromotionTask.setDelFlag(0);
            list.add(weShortLinkUserPromotionTask);
        }
        weShortLinkUserPromotionTaskService.saveBatch(list);

        //4.保存短链推广附件
        Optional.ofNullable(query.getAttachmentsList()).ifPresent(attachments -> {
            List<WeShortLinkPromotionAttachment> collect = attachments.stream().map(attachment -> {
                WeShortLinkPromotionAttachment weShortLinkPromotionAttachment = BeanUtil.copyProperties(attachment, WeShortLinkPromotionAttachment.class);
                //附件所属类型：0群发客户 1群发客群 2朋友圈
                weShortLinkPromotionAttachment.setTemplateType(1);
                weShortLinkPromotionAttachment.setTemplateId(templateGroup.getId());
                weShortLinkPromotionAttachment.setDelFlag(0);
                return weShortLinkPromotionAttachment;
            }).collect(Collectors.toList());
            weShortLinkPromotionAttachmentService.saveBatch(collect);
        });

        //5.保存短链推广发送结果
        Optional.ofNullable(query.getSenderList()).ifPresent(senderInfos -> {
            senderInfos.stream().forEach(senderInfo -> {
                List<WeShortLinkPromotionSendResult> sendResultList = new ArrayList<>();
                for (String chatId : senderInfo.getChatList()) {
                    WeShortLinkPromotionSendResult weShortLinkPromotionSendResult = new WeShortLinkPromotionSendResult();
                    //附件所属类型：0群发客户 1群发客群 2朋友圈
                    weShortLinkPromotionSendResult.setTemplateType(1);
                    weShortLinkPromotionSendResult.setTemplateId(templateGroup.getId());
                    weShortLinkPromotionSendResult.setUserId(senderInfo.getUserId());
                    weShortLinkPromotionSendResult.setChatId(chatId);
                    weShortLinkPromotionSendResult.setStatus(0);
                    weShortLinkPromotionSendResult.setDelFlag(0);
                    sendResultList.add(weShortLinkPromotionSendResult);
                }
                weShortLinkPromotionSendResultService.saveBatch(sendResultList);
            });
        });

        //6.mq发送
        if (sendType.equals(0)) {
            directSend(weShortLinkPromotion.getId(),
                    templateGroup.getId(),
                    templateGroup.getContent(),
                    query.getAttachmentsList(),
                    query.getSenderList());
        } else {
            timingSend(weShortLinkPromotion.getId(),
                    templateGroup.getId(),
                    templateGroup.getContent(),
                    Date.from(templateGroup.getTaskSendTime().atZone(ZoneId.systemDefault()).toInstant()),
                    query.getAttachmentsList(),
                    query.getSenderList());
        }
        //任务结束时间
        Optional.ofNullable(templateGroup.getTaskSendTime()).ifPresent(o -> {
            //TODO mq 定时结束
            timingEnd();
        });
        return weShortLinkPromotion.getId();
    }

    @Override
    public void updateAndSend(WeShortLinkPromotionUpdateQuery query, WeShortLinkPromotion weShortLinkPromotion) {

        WeShortLinkPromotionTemplateGroupUpdateQuery groupUpdateQuery = query.getGroup();

        //1.更新推广短链
        //发送类型：0立即发送 1定时发送
        Integer sendType = groupUpdateQuery.getSendType();
        if (sendType.equals(0)) {
            //任务状态: 0待推广 1推广中 2已结束 3暂存中
            weShortLinkPromotion.setTaskStatus(1);
            weShortLinkPromotion.setTaskStartTime(LocalDateTime.now());
        } else {
            weShortLinkPromotion.setTaskStatus(0);
            weShortLinkPromotion.setTaskStartTime(groupUpdateQuery.getTaskSendTime());
        }
        //任务结束时间
        LocalDateTime taskEndTime = groupUpdateQuery.getTaskEndTime();
        Optional.ofNullable(taskEndTime).ifPresent(o -> weShortLinkPromotion.setTaskEndTime(o));
        //保存短链推广
        weShortLinkPromotionService.updateById(weShortLinkPromotion);

        //2.更新推广短链模板-客群
        LambdaUpdateWrapper<WeShortLinkPromotionTemplateGroup> groupUpdateWrapper = Wrappers.lambdaUpdate();
        groupUpdateWrapper.set(WeShortLinkPromotionTemplateGroup::getDelFlag, 1);
        groupUpdateWrapper.eq(WeShortLinkPromotionTemplateGroup::getId, groupUpdateQuery.getId());
        weShortLinkPromotionTemplateGroupService.update(groupUpdateWrapper);
        WeShortLinkPromotionTemplateGroup templateGroup = BeanUtil.copyProperties(groupUpdateQuery, WeShortLinkPromotionTemplateGroup.class);
        templateGroup.setId(null);
        templateGroup.setPromotionId(weShortLinkPromotion.getId());
        templateGroup.setDelFlag(0);
        weShortLinkPromotionTemplateGroupService.save(templateGroup);

        //3.更新员工短链推广任务
        LambdaUpdateWrapper<WeShortLinkUserPromotionTask> userPromotionTaskUpdateWrapper = Wrappers.lambdaUpdate();
        userPromotionTaskUpdateWrapper.eq(WeShortLinkUserPromotionTask::getTemplateType, 1);
        userPromotionTaskUpdateWrapper.eq(WeShortLinkUserPromotionTask::getTemplateId, groupUpdateQuery.getId());
        userPromotionTaskUpdateWrapper.set(WeShortLinkUserPromotionTask::getDelFlag, 1);
        weShortLinkUserPromotionTaskService.update(userPromotionTaskUpdateWrapper);
        List<WeShortLinkUserPromotionTask> list = new ArrayList<>();
        List<WeAddGroupMessageQuery.SenderInfo> senderList = query.getSenderList();
        for (WeAddGroupMessageQuery.SenderInfo senderInfo : senderList) {
            WeShortLinkUserPromotionTask weShortLinkUserPromotionTask = new WeShortLinkUserPromotionTask();
            weShortLinkUserPromotionTask.setUserId(senderInfo.getUserId());
            //任务所属类型：0群发客户 1群发客群 2朋友圈
            weShortLinkUserPromotionTask.setTemplateType(1);
            weShortLinkUserPromotionTask.setTemplateId(templateGroup.getId());
            //员工发送状态
            weShortLinkUserPromotionTask.setSendStatus(0);
            weShortLinkUserPromotionTask.setAllGroupNum(senderInfo.getChatList().size());
            weShortLinkUserPromotionTask.setRealGroupNum(0);
            weShortLinkUserPromotionTask.setDelFlag(0);
            list.add(weShortLinkUserPromotionTask);
        }
        weShortLinkUserPromotionTaskService.saveBatch(list);

        //4.更新短链推广附件
        LambdaUpdateWrapper<WeShortLinkPromotionAttachment> attachmentUpdateWrapper = Wrappers.lambdaUpdate();
        attachmentUpdateWrapper.eq(WeShortLinkPromotionAttachment::getTemplateId, groupUpdateQuery.getId());
        attachmentUpdateWrapper.eq(WeShortLinkPromotionAttachment::getTemplateType, 1);
        attachmentUpdateWrapper.set(WeShortLinkPromotionAttachment::getDelFlag, 1);
        weShortLinkPromotionAttachmentService.update(attachmentUpdateWrapper);
        Optional.ofNullable(query.getAttachmentsList()).ifPresent(attachments -> {
            List<WeShortLinkPromotionAttachment> collect = attachments.stream().map(attachment -> {
                WeShortLinkPromotionAttachment weShortLinkPromotionAttachment = BeanUtil.copyProperties(attachment, WeShortLinkPromotionAttachment.class);
                //附件所属类型：0群发客户 1群发客群 2朋友圈
                weShortLinkPromotionAttachment.setTemplateType(1);
                weShortLinkPromotionAttachment.setTemplateId(templateGroup.getId());
                weShortLinkPromotionAttachment.setDelFlag(0);
                return weShortLinkPromotionAttachment;
            }).collect(Collectors.toList());
            weShortLinkPromotionAttachmentService.saveBatch(collect);
        });

        //5.更新短链推广发送结果
        LambdaUpdateWrapper<WeShortLinkPromotionSendResult> promotionSendResultUpdateWrapper = Wrappers.lambdaUpdate();
        promotionSendResultUpdateWrapper.eq(WeShortLinkPromotionSendResult::getTemplateType, 1);
        promotionSendResultUpdateWrapper.eq(WeShortLinkPromotionSendResult::getTemplateId, groupUpdateQuery.getId());
        promotionSendResultUpdateWrapper.set(WeShortLinkPromotionSendResult::getDelFlag, 1);
        weShortLinkPromotionSendResultService.update(promotionSendResultUpdateWrapper);
        Optional.ofNullable(query.getSenderList()).ifPresent(senderInfos -> {
            senderInfos.stream().forEach(senderInfo -> {
                List<WeShortLinkPromotionSendResult> sendResultList = new ArrayList<>();
                for (String chatId : senderInfo.getChatList()) {
                    WeShortLinkPromotionSendResult weShortLinkPromotionSendResult = new WeShortLinkPromotionSendResult();
                    //附件所属类型：0群发客户 1群发客群 2朋友圈
                    weShortLinkPromotionSendResult.setTemplateType(1);
                    weShortLinkPromotionSendResult.setTemplateId(templateGroup.getId());
                    weShortLinkPromotionSendResult.setUserId(senderInfo.getUserId());
                    weShortLinkPromotionSendResult.setChatId(chatId);
                    weShortLinkPromotionSendResult.setStatus(0);
                    weShortLinkPromotionSendResult.setDelFlag(0);
                    sendResultList.add(weShortLinkPromotionSendResult);
                }
                weShortLinkPromotionSendResultService.saveBatch(sendResultList);
            });
        });

        //6.mq发送
        if (sendType.equals(0)) {
            directSend(weShortLinkPromotion.getId(),
                    templateGroup.getId(),
                    templateGroup.getContent(),
                    query.getAttachmentsList(),
                    query.getSenderList());
        } else {
            timingSend(weShortLinkPromotion.getId(),
                    templateGroup.getId(),
                    templateGroup.getContent(),
                    Date.from(templateGroup.getTaskSendTime().atZone(ZoneId.systemDefault()).toInstant()),
                    query.getAttachmentsList(),
                    query.getSenderList());
        }
        //任务结束时间
        Optional.ofNullable(templateGroup.getTaskSendTime()).ifPresent(o -> {
            //TODO mq 定时结束
            timingEnd();
        });

    }

    @Override
    protected void directSend(Long id, Long businessId, String content, List<WeMessageTemplate> attachments, List<WeAddGroupMessageQuery.SenderInfo> senderList) {
        WeAddGroupMessageQuery weAddGroupMessageQuery = new WeAddGroupMessageQuery();
        weAddGroupMessageQuery.setId(id);
        weAddGroupMessageQuery.setChatType(2);
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
        weAddGroupMessageQuery.setChatType(2);
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
