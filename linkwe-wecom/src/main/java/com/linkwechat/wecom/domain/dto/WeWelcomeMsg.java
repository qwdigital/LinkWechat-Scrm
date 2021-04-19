package com.linkwechat.wecom.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author admin
 * @date 2020-11-18
 */
@Data
@Builder
public class WeWelcomeMsg {
    private String welcome_code;

    private Text text;

    private Image image;

    private Link link;

    private MiniProgram miniprogram;

    @Data
    @Builder
    public static class Text {
        /**
         * 消息文本内容,最长为4000字节
         */
        private String content;
    }

    @Data
    @Builder
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
    @Builder
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
    @Builder
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
}
