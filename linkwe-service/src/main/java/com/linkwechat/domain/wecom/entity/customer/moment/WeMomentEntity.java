package com.linkwechat.domain.wecom.entity.customer.moment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @Description 朋友圈
 * @date 2021/12/2 16:11
 **/
@ApiModel
@Data
public class WeMomentEntity {

    @ApiModelProperty("朋友圈id")
    private String momentId;

    @ApiModelProperty("朋友圈创建者userid")
    private String creator;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("朋友圈创建来源。0：企业 1：个人")
    private Integer createType;

    @ApiModelProperty("可见范围类型。0：部分可见 1：公开")
    private Integer visibleType;

    @ApiModelProperty("文本消息结构")
    private MomentText text;

    @ApiModelProperty("图片的media_id列表")
    private List<MomentImage> image;

    @ApiModelProperty("视频media_id")
    private List<MomentVideo> video;

    @ApiModelProperty("网页链接标题")
    private List<MomentLink> link;

    @ApiModelProperty("地理位置纬度")
    private List<MomentLocation> location;


    @ApiModel
    @Data
    private static class MomentText {
        @ApiModelProperty("文本消息")
        private String content;
    }

    @ApiModel
    @Data
    private static class MomentImage {
        @ApiModelProperty("图片的media_id")
        private String mediaId;
    }

    @ApiModel
    @Data
    private static class MomentVideo {
        @ApiModelProperty("视频media_id")
        private String mediaId;
        @ApiModelProperty("视频封面media_id")
        private String thumbMediaId;
    }

    @ApiModel
    @Data
    private static class MomentLink {
        @ApiModelProperty("网页链接标题")
        private String title;
        @ApiModelProperty("网页链接url")
        private String url;
    }

    @ApiModel
    @Data
    private static class MomentLocation {
        @ApiModelProperty("地理位置纬度")
        private String latitude;
        @ApiModelProperty("地理位置经度")
        private String longitude;
        @ApiModelProperty("地理位置名称")
        private String name;
    }
}
