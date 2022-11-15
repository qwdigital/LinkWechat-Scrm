package com.linkwechat.domain.wecom.query;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.linkwechat.common.enums.WeMsgTypeEnum;
import com.linkwechat.domain.media.WeMessageTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息发送的对象入参
 *
 * @author danmo
 * @date 2021-10-3
 */
@Data
public class WeMsgTemplateQuery extends WeBaseQuery{

    /**
     * 消息文本内容，最多4000个字节
     */
    private Text text;

    /**
     * 附件，最多支持添加9个附件
     */
    private List<Object> attachments;

    public void setAttachmentsList(String domain,List<WeMessageTemplate> messageTemplates) {
        this.attachments = new ArrayList<>(16);
        if (CollectionUtil.isNotEmpty(messageTemplates)) {
            messageTemplates.forEach(messageTemplate -> {
                if (ObjectUtil.equal(WeMsgTypeEnum.TEXT.getMessageType(), messageTemplate.getMsgType())) {
                    this.text = new Text();
                    text.setContent(messageTemplate.getContent());
                } else if (ObjectUtil.equal(WeMsgTypeEnum.IMAGE.getMessageType(), messageTemplate.getMsgType())) {
                    Attachments images = new Images(messageTemplate.getMsgType(), messageTemplate.getMediaId(),
                            messageTemplate.getPicUrl());
                    attachments.add(images);
                } else if (ObjectUtil.equal(WeMsgTypeEnum.LINK.getMessageType(), messageTemplate.getMsgType())) {
                    Attachments links = new Links(messageTemplate.getMsgType(), messageTemplate.getTitle(),
                            messageTemplate.getPicUrl(), messageTemplate.getDescription(), messageTemplate.getLinkUrl());
                    attachments.add(links);
                } else if (ObjectUtil.equal(WeMsgTypeEnum.MINIPROGRAM.getMessageType(), messageTemplate.getMsgType())) {
                    Attachments miniprograms = new Miniprograms(messageTemplate.getMsgType(), messageTemplate.getTitle(),
                            messageTemplate.getMediaId(), messageTemplate.getAppId(), messageTemplate.getLinkUrl());
                    attachments.add(miniprograms);
                } else if (ObjectUtil.equal(WeMsgTypeEnum.VIDEO.getMessageType(), messageTemplate.getMsgType())) { //转成h5

                    String linkUrl=domain+"/mobile/#/metrialDetail?mediaType="+WeMsgTypeEnum.VIDEO.getMessageType()+"&materialUrl="+messageTemplate.getLinkUrl();

                    Attachments links = new Links(WeMsgTypeEnum.LINK.getMessageType(), messageTemplate.getTitle(),
                            messageTemplate.getPicUrl(), messageTemplate.getDescription(),
                            linkUrl);
                    attachments.add(links);
//                    Attachments videos = new Videos(messageTemplate.getMsgType(), messageTemplate.getMediaId());
//                    attachments.add(videos);

                } else if (ObjectUtil.equal(WeMsgTypeEnum.FILE.getMessageType(), messageTemplate.getMsgType())) { //转成h5
                    String linkUrl=domain+"/mobile/#/metrialDetail?mediaType="+WeMsgTypeEnum.FILE.getMessageType()+"&materialUrl="+messageTemplate.getLinkUrl();

                    Attachments links = new Links(WeMsgTypeEnum.LINK.getMessageType(), messageTemplate.getTitle(),
                            messageTemplate.getPicUrl(), messageTemplate.getDescription(),
                            linkUrl);
                    attachments.add(links);

//                    Attachments files = new Files(messageTemplate.getMsgType(), messageTemplate.getMediaId());
//                    attachments.add(files);

                }
            });
        }
    }

    @Data
    public static class Attachments {
        /**
         * 附件类型，可选image、link、miniprogram或者video
         */
        private String msgtype;
    }

    @EqualsAndHashCode(callSuper = true)
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

    @EqualsAndHashCode(callSuper = true)
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

    @EqualsAndHashCode(callSuper = true)
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

    @EqualsAndHashCode(callSuper = true)
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

    @EqualsAndHashCode(callSuper = true)
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

    @Data
    public static class Text {
        /**
         * 文本消息内容
         */
        private String content;
    }
}
