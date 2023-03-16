package com.linkwechat.service.impl.strategic.shortlink;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeShortLinkPromotion;
import com.linkwechat.domain.WeShortLinkPromotionTemplateMoments;
import com.linkwechat.domain.WeShortLinkUserPromotionTask;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.moments.entity.WeMoments;
import com.linkwechat.domain.shortlink.dto.WeShortLinkPromotionMomentsDto;
import com.linkwechat.domain.shortlink.query.*;
import com.linkwechat.service.IWeShortLinkPromotionAttachmentService;
import com.linkwechat.service.IWeShortLinkPromotionService;
import com.linkwechat.service.IWeShortLinkPromotionTemplateMomentsService;
import com.linkwechat.service.IWeShortLinkUserPromotionTaskService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 朋友圈
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/10 11:48
 */
@Service
public class MomentsPromotion extends PromotionType {
    @Resource
    private IWeShortLinkPromotionService weShortLinkPromotionService;
    @Resource
    private IWeShortLinkPromotionTemplateMomentsService weShortLinkPromotionTemplateMomentsService;
    @Resource
    private IWeShortLinkPromotionAttachmentService weShortLinkPromotionAttachmentService;
    @Resource
    private IWeShortLinkUserPromotionTaskService weShortLinkUserPromotionTaskService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private RabbitMQSettingConfig rabbitMQSettingConfig;


    @Override
    public Long saveAndSend(WeShortLinkPromotionAddQuery query, WeShortLinkPromotion weShortLinkPromotion) {

        WeShortLinkPromotionTemplateMomentsAddQuery momentsAddQuery = query.getMoments();

        //1.保存短链推广
        //发送类型：0立即发送 1定时发送
        Integer sendType = momentsAddQuery.getSendType();
        if (sendType.equals(0)) {
            // 任务状态: 0待推广 1推广中 2已结束
            weShortLinkPromotion.setTaskStatus(1);
            weShortLinkPromotion.setTaskStartTime(LocalDateTime.now());
        } else {
            weShortLinkPromotion.setTaskStatus(0);
            weShortLinkPromotion.setTaskStartTime(momentsAddQuery.getTaskSendTime());
        }
        Optional.ofNullable(momentsAddQuery.getTaskEndTime()).ifPresent(i -> weShortLinkPromotion.setTaskEndTime(i));
        weShortLinkPromotionService.save(weShortLinkPromotion);

        //2.保存短链推广模板-朋友圈
        WeShortLinkPromotionTemplateMoments moments = BeanUtil.copyProperties(momentsAddQuery, WeShortLinkPromotionTemplateMoments.class);
        moments.setPromotionId(weShortLinkPromotion.getId());
        moments.setDelFlag(0);
        weShortLinkPromotionTemplateMomentsService.save(moments);

        //3.保存短链推广附件
        //不需要

        //4.保存员工推广短链任务
        WeShortLinkUserPromotionTask weShortLinkUserPromotionTask = new WeShortLinkUserPromotionTask();
        //群发朋友圈分类 0全部客户 1部分客户
        Integer scopeType = momentsAddQuery.getScopeType();
        if (scopeType.equals(1)) {
            //部分客户
            weShortLinkUserPromotionTask.setUserId(momentsAddQuery.getUserIds());
        }
        weShortLinkUserPromotionTask.setTemplateType(2);
        weShortLinkUserPromotionTask.setTemplateId(moments.getId());
        weShortLinkUserPromotionTask.setSendStatus(0);
        weShortLinkUserPromotionTask.setDelFlag(0);
        weShortLinkUserPromotionTaskService.save(weShortLinkUserPromotionTask);


        //5.发送mq
        String userIds = momentsAddQuery.getUserIds();
        List<WeAddGroupMessageQuery.SenderInfo> senderInfos = new ArrayList<>();
        Arrays.stream(userIds.split(",")).forEach(i -> {
            WeAddGroupMessageQuery.SenderInfo senderInfo = new WeAddGroupMessageQuery.SenderInfo();
            senderInfo.setUserId(i);
            senderInfos.add(senderInfo);
        });

        if (sendType.equals(0)) {
            directSend(weShortLinkPromotion.getId(), moments.getId(), momentsAddQuery.getContent(), query.getAttachmentsList(), senderInfos, momentsAddQuery.getLabelIds());
        } else {
            timingSend(weShortLinkPromotion.getId(),
                    moments.getId(),
                    momentsAddQuery.getContent(),
                    Date.from(query.getClient().getTaskSendTime().atZone(ZoneId.systemDefault()).toInstant()),
                    query.getAttachmentsList(),
                    senderInfos,
                    momentsAddQuery.getLabelIds()
            );
        }

        //任务结束时间
        LocalDateTime taskEndTime = momentsAddQuery.getTaskEndTime();
        Optional.ofNullable(taskEndTime).ifPresent(o -> {
            timingEnd(weShortLinkPromotion.getId(), moments.getId(), weShortLinkPromotion.getType(), Date.from(taskEndTime.atZone(ZoneId.systemDefault()).toInstant()));
        });

        return weShortLinkPromotion.getId();
    }

    @Override
    public void updateAndSend(WeShortLinkPromotionUpdateQuery query, WeShortLinkPromotion weShortLinkPromotion) {
        //1.更新短链推广
        WeShortLinkPromotionTemplateMomentsUpdateQuery momentsUpdateQuery = query.getMoments();
        //发送类型：0立即发送 1定时发送
        Integer sendType = momentsUpdateQuery.getSendType();
        if (sendType.equals(0)) {
            //任务状态: 0待推广 1推广中 2已结束 3暂存中
            weShortLinkPromotion.setTaskStatus(1);
            weShortLinkPromotion.setTaskStartTime(LocalDateTime.now());
        } else {
            weShortLinkPromotion.setTaskStatus(0);
            weShortLinkPromotion.setTaskStartTime(momentsUpdateQuery.getTaskSendTime());
        }
        //任务结束时间
        LocalDateTime taskEndTime = momentsUpdateQuery.getTaskEndTime();
        Optional.ofNullable(taskEndTime).ifPresent(o -> weShortLinkPromotion.setTaskEndTime(o));
        //保存短链推广
        weShortLinkPromotionService.updateById(weShortLinkPromotion);

        //2.更新短链推广模板-朋友圈
        //删除
        LambdaUpdateWrapper<WeShortLinkPromotionTemplateMoments> momentsUpdateWrapper = Wrappers.lambdaUpdate();
        momentsUpdateWrapper.set(WeShortLinkPromotionTemplateMoments::getDelFlag, 1);
        momentsUpdateWrapper.eq(WeShortLinkPromotionTemplateMoments::getPromotionId, weShortLinkPromotion.getId());
        weShortLinkPromotionTemplateMomentsService.update(momentsUpdateWrapper);
        //添加
        WeShortLinkPromotionTemplateMoments moments = BeanUtil.copyProperties(momentsUpdateQuery, WeShortLinkPromotionTemplateMoments.class);
        moments.setId(null);
        moments.setPromotionId(weShortLinkPromotion.getId());
        moments.setDelFlag(0);
        weShortLinkPromotionTemplateMomentsService.save(moments);

        //3.更新员工推广短链任务
        LambdaUpdateWrapper<WeShortLinkUserPromotionTask> userPromotionTaskUpdateWrapper = Wrappers.lambdaUpdate();
        userPromotionTaskUpdateWrapper.eq(WeShortLinkUserPromotionTask::getTemplateType, 2);
        userPromotionTaskUpdateWrapper.eq(WeShortLinkUserPromotionTask::getTemplateId, momentsUpdateQuery.getId());
        userPromotionTaskUpdateWrapper.set(WeShortLinkUserPromotionTask::getDelFlag, 1);
        weShortLinkUserPromotionTaskService.update(userPromotionTaskUpdateWrapper);
        //添加
        //群发朋友圈分类 0全部客户 1部分客户
        Integer scopeType = momentsUpdateQuery.getScopeType();
        //部分客户
        if (scopeType.equals(1)) {
            String userIds = momentsUpdateQuery.getUserIds();
            WeShortLinkUserPromotionTask weShortLinkUserPromotionTask = new WeShortLinkUserPromotionTask();
            weShortLinkUserPromotionTask.setUserId(userIds);
            weShortLinkUserPromotionTask.setTemplateType(2);
            weShortLinkUserPromotionTask.setTemplateId(moments.getId());
            weShortLinkUserPromotionTask.setSendStatus(0);
            weShortLinkUserPromotionTask.setDelFlag(0);
            weShortLinkUserPromotionTaskService.save(weShortLinkUserPromotionTask);
        }

        //4.发送mq
        String userIds = momentsUpdateQuery.getUserIds();
        List<WeAddGroupMessageQuery.SenderInfo> senderInfos = new ArrayList<>();
        Arrays.stream(userIds.split(",")).forEach(i -> {
            WeAddGroupMessageQuery.SenderInfo senderInfo = new WeAddGroupMessageQuery.SenderInfo();
            senderInfo.setUserId(i);
            senderInfos.add(senderInfo);
        });

        if (sendType.equals(0)) {
            directSend(weShortLinkPromotion.getId(), moments.getId(), momentsUpdateQuery.getContent(), query.getAttachmentsList(), senderInfos, momentsUpdateQuery.getLabelIds());
        } else {
            timingSend(weShortLinkPromotion.getId(),
                    moments.getId(),
                    momentsUpdateQuery.getContent(),
                    Date.from(query.getClient().getTaskSendTime().atZone(ZoneId.systemDefault()).toInstant()),
                    query.getAttachmentsList(),
                    senderInfos,
                    momentsUpdateQuery.getLabelIds()
            );
        }

        //任务结束时间
        Optional.ofNullable(taskEndTime).ifPresent(o -> {
            timingEnd(weShortLinkPromotion.getId(), moments.getId(), weShortLinkPromotion.getType(), Date.from(taskEndTime.atZone(ZoneId.systemDefault()).toInstant()));
        });


    }

    @Override
    protected void directSend(Long id, Long businessId, String content, List<WeMessageTemplate> attachments, List<WeAddGroupMessageQuery.SenderInfo> senderList, Object... objects) {
        WeShortLinkPromotionMomentsDto weMoments = new WeShortLinkPromotionMomentsDto();

        weMoments.setShortLinkPromotionId(id);
        weMoments.setBusinessId(businessId);
        weMoments.setLoginUser(SecurityUtils.getLoginUser());

        weMoments.setContent(content);
        //可见类型:0:部分可见;1:公开;
        if (senderList.size() > 0) {
            weMoments.setScopeType(0);
            //发送者
            String collect = senderList.stream().map(i -> i.getUserId()).collect(Collectors.joining(","));
            weMoments.setNoAddUser(collect);
        } else {
            weMoments.setScopeType(1);
        }

        //客户标签
        Object object = objects[0];
        if (BeanUtil.isNotEmpty(object)) {
            weMoments.setCustomerTag(String.valueOf(object));
        }
        //附件
        List<WeMoments.OtherContent> otherContents = new ArrayList<>();
        attachments.stream().findFirst().ifPresent(i -> {
            WeMoments.OtherContent otherContent = new WeMoments.OtherContent();
            otherContent.setAnnexType(MediaType.IMAGE.getMediaType());
            otherContent.setAnnexUrl(i.getPicUrl());
            otherContents.add(otherContent);
        });
        weMoments.setOtherContent(otherContents);
        weMoments.setContentType(MediaType.IMAGE.getMediaType());

        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeMomentMsgRk(), JSONObject.toJSONString(weMoments));

    }

    @Override
    protected void timingSend(Long id, Long businessId, String content, Date sendTime, List<WeMessageTemplate> attachments, List<WeAddGroupMessageQuery.SenderInfo> senderList, Object... objects) {
        WeShortLinkPromotionMomentsDto weMoments = new WeShortLinkPromotionMomentsDto();

        weMoments.setShortLinkPromotionId(id);
        weMoments.setBusinessId(businessId);
        weMoments.setLoginUser(SecurityUtils.getLoginUser());

        weMoments.setContent(content);
        //可见类型:0:部分可见;1:公开;
        if (senderList.size() > 0) {
            weMoments.setScopeType(0);
            //发送者
            String collect = senderList.stream().map(i -> i.getUserId()).collect(Collectors.joining(","));
            weMoments.setNoAddUser(collect);
        } else {
            weMoments.setScopeType(1);
        }

        //客户标签
        Object object = objects[0];
        if (BeanUtil.isNotEmpty(object)) {
            weMoments.setCustomerTag(String.valueOf(object));
        }

        //附件
        List<WeMoments.OtherContent> otherContents = new ArrayList<>();
        attachments.stream().findFirst().ifPresent(i -> {
            WeMoments.OtherContent otherContent = new WeMoments.OtherContent();
            otherContent.setAnnexType(MediaType.IMAGE.getMediaType());
            otherContent.setAnnexUrl(i.getPicUrl());
        });
        weMoments.setOtherContent(otherContents);

        long diffTime = DateUtils.diffTime(sendTime, new Date());
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeDelayMomentMsgRk(), JSONObject.toJSONString(weMoments), message -> {
            //注意这里时间可使用long类型,毫秒单位，设置header
            message.getMessageProperties().setHeader("x-delay", diffTime);
            return message;
        });
    }

    @Override
    protected void timingEnd(Long promotionId, Long businessId, Integer type, Date taskEndTime) {
        WeShortLinkPromotionTaskEndQuery query = new WeShortLinkPromotionTaskEndQuery();
        query.setPromotionId(promotionId);
        query.setBusinessId(businessId);
        query.setType(type);
        long diffTime = DateUtils.diffTime(taskEndTime, new Date());
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeDelayGroupMsgEndRk(), JSONObject.toJSONString(query), message -> {
            //注意这里时间可使用long类型,毫秒单位，设置header
            message.getMessageProperties().setHeader("x-delay", diffTime);
            return message;
        });
    }
}
