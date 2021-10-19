package com.linkwechat.wecom.domain.query;

import cn.hutool.core.util.ObjectUtil;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.wecom.domain.WeMessageTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息发送的对象入参
 *
 * @author danmo
 * @date 2021-10-3
 */
@ApiModel
@Data
public class WeAddMsgTemplateQuery {

    @ApiModelProperty("群发任务的类型，默认为single，表示发送给客户，group表示发送给客户群")
    private String chat_type;

    @ApiModelProperty("客户的外部联系人id列表，仅在chat_type为single时有效，不可与sender同时为空，最多可传入1万个客户")
    private List<String> external_userid;

    @ApiModelProperty("发送企业群发消息的成员userid，当类型为发送给客户群时必填")
    private String sender;

    @ApiModelProperty("消息文本内容，最多4000个字节")
    private Text text;

    @ApiModelProperty("附件，最多支持添加9个附件 ru")
    private List<Attachments> attachments;


    public void setAttachments(List<WeMessageTemplate> messageTemplates) {
        final int size = 9;
        this.attachments = new ArrayList<>(16);
        if (messageTemplates != null && messageTemplates.size() <= size) {
            messageTemplates.forEach(messageTemplate -> {
                if (ObjectUtil.equal("image", messageTemplate.getMsgType())) {
                    Attachments images = new Images(messageTemplate.getMsgType(), messageTemplate.getMediaId(),
                            messageTemplate.getPicUrl());
                    attachments.add(images);
                } else if (ObjectUtil.equal("link", messageTemplate.getMsgType())) {
                    Attachments links = new Links(messageTemplate.getMsgType(), messageTemplate.getTitle(),
                            messageTemplate.getPicUrl(), messageTemplate.getDescription(), messageTemplate.getLinkUrl());
                    attachments.add(links);
                } else if (ObjectUtil.equal("miniprogram", messageTemplate.getMsgType())) {
                    Attachments miniprograms = new Miniprograms(messageTemplate.getMsgType(), messageTemplate.getTitle(),
                            messageTemplate.getMediaId(), messageTemplate.getAppId(), messageTemplate.getLinkUrl());
                    attachments.add(miniprograms);
                } else if (ObjectUtil.equal("video", messageTemplate.getMsgType())) {
                    Attachments videos = new Videos(messageTemplate.getMsgType(), messageTemplate.getMediaId());
                    attachments.add(videos);
                } else if (ObjectUtil.equal("file", messageTemplate.getMsgType())) {
                    Attachments files = new Files(messageTemplate.getMsgType(), messageTemplate.getMediaId());
                    attachments.add(files);
                }
            });
        } else {
            throw new WeComException("仅支持最多同时发送9个附件");
        }
    }

    @Data
    public static class Attachments {
        @ApiModelProperty("附件类型，可选image、link、miniprogram或者video")
        private String msgtype;
    }

    @Data
    public static class Images extends Attachments {
        private Image image;

        public Images(String msgType, String mediaId, String picUrl) {
            super.setMsgtype(msgType);
            this.image = new Image();
            this.image.setMedia_id(mediaId);
            this.image.setPic_url(picUrl);
        }

        @Data
        public static class Image {
            private String media_id;
            private String pic_url;
        }
    }

    @Data
    public static class Links extends Attachments {
        private Link link;

        public Links(String msgType, String title, String picUrl, String desc, String url) {
            super.setMsgtype(msgType);
            this.link = new Link();
            this.link.setTitle(title);
            this.link.setPicurl(picUrl);
            this.link.setDesc(desc);
            this.link.setUrl(url);
        }

        @Data
        public static class Link {
            private String title;
            private String picurl;
            private String desc;
            private String url;
        }
    }

    @Data
    public static class Miniprograms extends Attachments {
        private Miniprogram miniprogram;

        public Miniprograms(String msgType, String title, String mediaId, String appId, String page) {
            super.setMsgtype(msgType);
            this.miniprogram = new Miniprogram();
            this.miniprogram.setTitle(title);
            this.miniprogram.setPic_media_id(mediaId);
            this.miniprogram.setAppid(appId);
            this.miniprogram.setPage(page);
        }

        @Data
        public static class Miniprogram {
            private String title;
            private String pic_media_id;
            private String appid;
            private String page;
        }
    }

    @Data
    public static class Videos extends Attachments {
        private Video video;

        public Videos(String msgType, String mediaId) {
            super.setMsgtype(msgType);
            this.video = new Video();
            this.video.setMedia_id(mediaId);
        }

        @Data
        public static class Video {
            private String media_id;
        }
    }

    @Data
    public static class Files extends Attachments {
        private File file;

        public Files(String msgType, String mediaId) {
            super.setMsgtype(msgType);
            this.file = new File();
            this.file.setMedia_id(mediaId);
        }

        @Data
        public static class File {
            private String media_id;
        }
    }

    @ApiModel
    @Data
    public static class Text {
        @ApiModelProperty("文本消息内容")
        private String content;
    }
}
