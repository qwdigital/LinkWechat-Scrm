package com.linkwechat.scheduler.listener;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.moments.dto.MomentsParamDto;
import com.linkwechat.domain.moments.entity.WeMoments;
import com.linkwechat.fegin.QwMomentsClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.service.IWeMaterialService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 朋友圈 监听
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/13 11:23
 */
@Slf4j
@Component
public class QwMomentsMsgListener {

    @Resource
    private QwMomentsClient qwMomentsClient;

    @Resource
    private QwSysUserClient qwSysUserClient;


    @Autowired
    private IWeMaterialService iWeMaterialService;

    /**
     * 朋友圈正常消息监听
     *
     * @param msg
     * @param channel
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.moments-msg:Qu_Moments}")
    public void normalMsgSubscribe(String msg, Channel channel, Message message) {
        try {
            log.info("朋友圈正常消息监听：msg:{}", msg);
            WeMoments weMoments = JSONObject.parseObject(msg, WeMoments.class);
            sendMoments(weMoments);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("朋友圈正常消息监听-消息处理失败 msg:{},error:{}", msg, e);
        }
    }


    /**
     * 朋友圈延迟消息监听
     *
     * @param msg
     * @param channel
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = "${wecom.mq.queue.delay-moments-msg:Qu_DelayMomentsMsg}")
    public void delayMsgSubscribe(String msg, Channel channel, Message message) {
        try {
            log.info("朋友圈延迟消息监听：msg:{}", msg);
            WeMoments weMoments = JSONObject.parseObject(msg, WeMoments.class);
            sendMoments(weMoments);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("朋友圈正常消息监听-消息处理失败 msg:{},error:{}", msg, e);
        }
    }

    /**
     * 发送朋友圈
     *
     * @param weMoments
     */
    public void sendMoments(WeMoments weMoments) {
        MomentsParamDto momentsParamDto = new MomentsParamDto();
        if (StringUtils.isNotEmpty(weMoments.getContent())) {
            momentsParamDto.setText(MomentsParamDto.Text.builder().content(weMoments.getContent()).build());
        }
        //设置附件
        List<WeMoments.OtherContent> otherContent = weMoments.getOtherContent();
        if (CollectionUtil.isNotEmpty(otherContent)) {
            List<WeMoments.OtherContent> otherContents = otherContent.stream().filter(s -> StringUtils.isNotEmpty(s.getAnnexType()) && StringUtils.isNotEmpty(s.getAnnexUrl())).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(otherContents)) {
                List<Object> attachments = new ArrayList<>();
                //图片
                if (weMoments.getContentType().equals(MediaType.IMAGE.getMediaType())) {
                    otherContents.stream().forEach(image -> {
                        String media_id = iWeMaterialService.uploadAttachmentMaterial(image.getAnnexUrl(), MediaType.IMAGE.getMediaType(), 1, SnowFlakeUtil.nextId().toString()).getMediaId();
                        if (StringUtils.isNotEmpty(media_id)) {
                            attachments.add(MomentsParamDto.ImageAttachments.builder().msgtype(MediaType.IMAGE.getMediaType()).image(MomentsParamDto.Image.builder().media_id(media_id).build()).build());
                        }
                    });
                }
                //视频
                if (weMoments.getContentType().equals(MediaType.VIDEO.getMediaType())) {
                    otherContents.stream().forEach(video -> {
                        String media_id = iWeMaterialService.uploadAttachmentMaterial(video.getAnnexUrl(), MediaType.VIDEO.getMediaType(), 1, SnowFlakeUtil.nextId().toString()).getMediaId();
                        if (StringUtils.isNotEmpty(media_id)) {
                            attachments.add(MomentsParamDto.VideoAttachments.builder().msgtype(MediaType.VIDEO.getMediaType()).video(MomentsParamDto.Video.builder().media_id(media_id).build()).build());
                        }
                    });
                }
                //网页链接
                if (weMoments.getContentType().equals(MediaType.LINK.getMediaType())) {
                    otherContents.stream().forEach(link -> {
                        String media_id = iWeMaterialService.uploadAttachmentMaterial(link.getOther(), MediaType.LINK.getMediaType(), 1, SnowFlakeUtil.nextId().toString()).getMediaId();
                        if (StringUtils.isNotEmpty(media_id)) {
                            MomentsParamDto.Link build = new MomentsParamDto.Link();
                            build.setUrl(link.getAnnexUrl());
                            build.setMedia_id(media_id);
                            if (StringUtils.isNotEmpty(link.getTitle())) {
                                build.setTitle(link.getTitle());
                            }
                            attachments.add(MomentsParamDto.LinkAttachments.builder().msgtype(MediaType.LINK.getMediaType()).link(build).build());
                        }
                    });
                }
                momentsParamDto.setAttachments(attachments);
            }
        }
        MomentsParamDto.VisibleRange visibleRange = MomentsParamDto.VisibleRange.builder().build();

        //设置可见范围
        //部分
        if (weMoments.getScopeType().equals(new Integer(0))) {
            //客户标签
            if (StringUtils.isNotEmpty(weMoments.getCustomerTag())) {
                visibleRange.setExternal_contact_list(MomentsParamDto.ExternalContactList.builder().tag_list(weMoments.getCustomerTag().split(",")).build());
            }
            //指定发送人
            if (StringUtils.isNotEmpty(weMoments.getNoAddUser())) {
                visibleRange.setSender_list(MomentsParamDto.SenderList.builder().user_list(weMoments.getNoAddUser().split(",")).build());
            }
        } else {
            //全部
            SysUser sysUser = new SysUser();
            List<SysUser> weUsers = qwSysUserClient.list(sysUser).getData();
            if (CollectionUtil.isNotEmpty(weUsers)) {
                visibleRange.setSender_list(MomentsParamDto.SenderList.builder().user_list(weUsers.stream().map(SysUser::getWeUserId).toArray(String[]::new)).build());
            }
        }
        momentsParamDto.setVisible_range(visibleRange);
        qwMomentsClient.addMomentTask(momentsParamDto);
    }


}
