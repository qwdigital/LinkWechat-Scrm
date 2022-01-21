package com.linkwechat.wecom.domain.dto;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.wecom.domain.WeMessageTemplate;
import com.linkwechat.wecom.domain.WeQrAttachments;
import com.linkwechat.wecom.domain.query.WeAddMsgTemplateQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.SuperBuilder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2020-11-18
 */
@Data
@SuperBuilder
public class WeWelcomeMsg {
    private String welcome_code;

    private Text text;

    private Image image;

    private Link link;

    private MiniProgram miniprogram;

    private List<WeAddMsgTemplateQuery.Attachments> attachments;

    @Data
    @SuperBuilder
    public static class Text {
        /**
         * 消息文本内容,最长为4000字节
         */
        private String content;
    }

    @Data
    @SuperBuilder
    public static class Image {
        /**
         * 图片的media_id
         */
        private String media_id;
        /**
         * 图片的链接，仅可使用上传图片接口得到的链接
         */
        private String pic_url;
    }

    @Data
    @SuperBuilder
    public static class Link {
        /**
         * 图文消息标题，最长为128字节
         */
        private String title;
        /**
         * 图文消息封面的url
         */
        private String picurl;
        /**
         * 图文消息的描述，最长为512字节
         */
        private String desc;
        /**
         * 图文消息的链接
         */
        private String url;
    }

    @Data
    @SuperBuilder
    public static class MiniProgram {
        /**
         * 小程序消息标题，最长为64字节
         */
        private String title;
        /**
         * 小程序消息封面的mediaid，封面图建议尺寸为520*416
         */
        private String pic_media_id;
        /**
         * 小程序appid，必须是关联到企业的小程序应用
         */
        private String appid;
        /**
         * 小程序page路径
         */
        private String page;
    }

    public void setAttachments(List<WeQrAttachments> messageTemplates) {
        this.attachments = new ArrayList<>(16);
        if (CollectionUtil.isNotEmpty(messageTemplates)) {
            messageTemplates.forEach(messageTemplate -> {
                if (ObjectUtil.equal(MessageType.IMAGE.getMessageType(), messageTemplate.getMsgType())) {
                    WeAddMsgTemplateQuery.Attachments images = new WeAddMsgTemplateQuery.Images(messageTemplate.getMsgType(), messageTemplate.getMediaId(),
                            messageTemplate.getPicUrl());
                    attachments.add(images);
                } else if (ObjectUtil.equal(MessageType.LINK.getMessageType(), messageTemplate.getMsgType())) {
                    WeAddMsgTemplateQuery.Attachments links = new WeAddMsgTemplateQuery.Links(messageTemplate.getMsgType(), messageTemplate.getTitle(),
                            messageTemplate.getPicUrl(), messageTemplate.getDescription(), messageTemplate.getLinkUrl());
                    attachments.add(links);
                } else if (ObjectUtil.equal(MessageType.MINIPROGRAM.getMessageType(), messageTemplate.getMsgType())) {
                    WeAddMsgTemplateQuery.Attachments miniprograms = new WeAddMsgTemplateQuery.Miniprograms(messageTemplate.getMsgType(), messageTemplate.getTitle(),
                            messageTemplate.getMediaId(), messageTemplate.getAppId(), messageTemplate.getLinkUrl());
                    attachments.add(miniprograms);
                } else if (ObjectUtil.equal(MessageType.VIDEO.getMessageType(), messageTemplate.getMsgType())) {
                    WeAddMsgTemplateQuery.Attachments videos = new WeAddMsgTemplateQuery.Videos(messageTemplate.getMsgType(), messageTemplate.getMediaId());
                    attachments.add(videos);
                } else if (ObjectUtil.equal(MessageType.FILE.getMessageType(), messageTemplate.getMsgType())) {
                    WeAddMsgTemplateQuery.Attachments files = new WeAddMsgTemplateQuery.Files(messageTemplate.getMsgType(), messageTemplate.getMediaId());
                    attachments.add(files);
                }
            });
        }
    }
}
