package com.linkwechat.wecom.domain.dto.moments;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * 朋友圈列表详情
 */
@Data
public class MomentsListDetailResultDto extends WeResultDto {

    private String next_cursor;

    //朋友圈列表
    private List<Moment> moment_list;


    @Data
    public static class Moment{
        //朋友圈id
        private String moment_id;
        //朋友圈创建者userid,企业发表内容到客户的朋友圈接口创建的朋友圈不再返回该字段
        private String creator;
        //创建时间
        private Date create_time;
        //朋友圈创建来源。0：企业 1：个人
        private Integer create_type;
        //可见范围类型。0：部分可见 1：公开
        private Integer visible_type;
        //文本消息
        private Text text;
        //图片消息
        private List<MediaId> image;
        //视频消息
        private Video video;
        //链接消息
        private Link link;
        //位置消息
        private Location location;
    }


    //文本
    @Data
    public static class  Text{

        private String content;//文本内容

    }


    //视频
    @Data
    public static class Video{
        private String media_id;//视频media_id
        private String thumb_media_id;//视频封面media_id
    }

    //链接
    @Data
    public static class Link{
        //网页链接标题
        private String title;
        //地理位置纬度
        private String url;

    }

    //位置
    @Data
    public static class Location{

        //地理位置纬度
        private String latitude;

        //地理位置经度
        private String longitude;

        //地理位置名称
        private String name;

    }

    //图片
    @Data
    public static class MediaId{
        //图片的media_id列表
        private String media_id;

    }
}
