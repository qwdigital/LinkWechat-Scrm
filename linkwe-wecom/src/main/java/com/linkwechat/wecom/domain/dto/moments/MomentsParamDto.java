package com.linkwechat.wecom.domain.dto.moments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 朋友圈如参相关
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MomentsParamDto {


    //文本
    private Text text;

    //附件
    private List<BaseAttachments> attachments;

    //可见范围
    private VisibleRange visible_range;






    @Data
    @Builder
    public static  class  Text{
        private String content;
    }

    /**
     * 附件上级类
     */
    @Data
    public static  class  BaseAttachments{
        //类型:image:图片;video:视频;link:图文;
        private String msgtype;
    }


    /**
     * 图片附件
     */
    @Data
    @Builder
    public static  class  ImageAttachments extends BaseAttachments {

        private  Image image;



    }

    /**
     * 视频附件
     */
    @Data
    @Builder
    public static  class VideoAttachments extends BaseAttachments{
        private Video video;
    }


    /**
     * 图文附件
     */
    @Data
    @Builder
    public static class LinkAttachments extends BaseAttachments{
        private Link link;
    }




    @Data
    @Builder
    public static class Image{
        private String mediaId;
    }


    @Data
    @Builder
    public static  class Video{
        private String mediaId;
    }



    @Data
    @Builder
    public static class  Link{
        private String title;
        private String url;
        private String mediaId;
    }


    /****************************************
     ******************范围相关***************
     ***************************************/

    @Data
    @Builder
    public static  class  VisibleRange{


        private  SenderList sender_list;

        private ExternalContactList external_contact_list;

    }

    @Data
    @Builder
    public static  class  SenderList{

        private String[] user_list;

        private String[] department_list;

    }

    @Data
    @Builder
    public static class  ExternalContactList{

        private String[] tag_list;
    }



}
