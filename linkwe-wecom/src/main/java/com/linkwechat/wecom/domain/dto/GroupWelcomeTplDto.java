package com.linkwechat.wecom.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 入群欢迎语模版
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GroupWelcomeTplDto extends WeResultDto{


    //模版id
    private String template_id;

    private Text text;

    private Image image;

    private Link link;

    private Miniprogram miniprogram;


    private File file;

    private Video video;

    private Long agentid;

    private Integer notify;



    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Text{
        private String content;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static  class  Image{
        private String media_id;
        private String pic_url;

    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Link{
        private String title;
        private String picurl;
        private String desc;
        private String url;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Miniprogram{
        private String title;
        private String pic_media_id;
        private String appid;
        private String page;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static  class  File{
        private String media_id;
    }


    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Video{
        private String media_id;
    }

}
