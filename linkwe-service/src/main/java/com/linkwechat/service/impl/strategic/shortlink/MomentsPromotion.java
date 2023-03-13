package com.linkwechat.service.impl.strategic.shortlink;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeShortLinkPromotion;
import com.linkwechat.domain.WeShortLinkPromotionTemplateMoments;
import com.linkwechat.domain.WeShortLinkUserPromotionTask;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.moments.entity.WeMoments;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionTemplateMomentsAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionUpdateQuery;
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
        //群发朋友圈分类 0全部客户 1部分客户
        Integer scopeType = momentsAddQuery.getScopeType();
        //部分客户
        if (scopeType.equals(1)) {
            String userIds = momentsAddQuery.getUserIds();
            List<WeShortLinkUserPromotionTask> list = new ArrayList<>();
            for (String userId : userIds.split(",")) {
                WeShortLinkUserPromotionTask weShortLinkUserPromotionTask = new WeShortLinkUserPromotionTask();
                weShortLinkUserPromotionTask.setUserId(userId);
                weShortLinkUserPromotionTask.setTemplateType(2);
                weShortLinkUserPromotionTask.setSendStatus(0);
                weShortLinkUserPromotionTask.setDelFlag(0);
                list.add(weShortLinkUserPromotionTask);
            }
            weShortLinkUserPromotionTaskService.saveBatch(list);
        }

        //5.发送mq
        String userIds = momentsAddQuery.getUserIds();
        List<WeAddGroupMessageQuery.SenderInfo> senderInfos = new ArrayList<>();
        Arrays.stream(userIds.split(",")).forEach(i -> {
            WeAddGroupMessageQuery.SenderInfo senderInfo = new WeAddGroupMessageQuery.SenderInfo();
            senderInfo.setUserId(i);
            senderInfos.add(senderInfo);
        });

        if (sendType.equals(0)) {
            directSend(weShortLinkPromotion.getId(), moments.getId(), momentsAddQuery.getContent(), query.getAttachmentsList(), senderInfos);
        } else {
            timingSend(weShortLinkPromotion.getId(),
                    moments.getId(),
                    momentsAddQuery.getContent(),
                    Date.from(query.getClient().getTaskSendTime().atZone(ZoneId.systemDefault()).toInstant()),
                    query.getAttachmentsList(),
                    senderInfos
            );
        }


        return weShortLinkPromotion.getId();
    }

    @Override
    public void updateAndSend(WeShortLinkPromotionUpdateQuery query, WeShortLinkPromotion weShortLinkPromotion) {

    }

    @Override
    protected void directSend(Long id, Long businessId, String content, List<WeMessageTemplate> attachments, List<WeAddGroupMessageQuery.SenderInfo> senderList) {
        WeMoments weMoments = new WeMoments();

        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeMomentMsgRk(), new String());

    }

    @Override
    protected void timingSend(Long id, Long businessId, String content, Date sendTime, List<WeMessageTemplate> attachments, List<WeAddGroupMessageQuery.SenderInfo> senderList) {
        WeMoments weMoments = new WeMoments();

        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeDelayMomentMsgRk(), JSONObject.toJSONString(weMoments), message -> {
            //注意这里时间可使用long类型,毫秒单位，设置header
            message.getMessageProperties().setHeader("x-delay", null);
            return message;
        });
    }

    @Override
    protected void timingEnd() {

    }
}
