package com.linkwechat.domain.moments.dto;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 朋友圈如参相关
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MomentsParamDto  extends WeBaseQuery {


    //文本
    private Text text;

    //附件
    private List<Object> attachments;

    //可见范围
    private VisibleRange visible_range;


    private String moment_id;

    private String userid;

    private String cursor;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Text {
        private String content;
    }

    /**
     * 附件上级类
     */
    @Data
    public static class BaseAttachments {

    }


    /**
     * 图片附件
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImageAttachments extends BaseAttachments {
        private String msgtype;
        private Image image;


    }

    /**
     * 视频附件
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VideoAttachments extends BaseAttachments {
        public String msgtype;
        private Video video;
    }


    /**
     * 图文附件
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LinkAttachments extends BaseAttachments {
        public String msgtype;
        private Link link;
    }


    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Image {
        private String media_id;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Video {
        private String media_id;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Link {
        private String title;
        private String url;
        private String media_id;
    }


    /****************************************
     ******************范围相关***************
     ***************************************/

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VisibleRange {


        private SenderList sender_list;

        private ExternalContactList external_contact_list;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SenderList {

        private String[] user_list;

        private String[] department_list;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExternalContactList {

        private String[] tag_list;
    }


}
