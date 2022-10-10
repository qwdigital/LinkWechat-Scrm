package com.linkwechat.domain.wecom.vo.media;

import com.dtflys.forest.http.ForestResponse;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.InputStream;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/24 0024 0:16
 */
@ApiModel
@Data
public class WeMediaVo extends WeResultVo {
    /**
     * 媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件(file)
     */
    @ApiModelProperty("媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件(file)")
    private String type;
    /**
     * 媒体文件上传后获取的唯一标识，3天内有效
     */
    @ApiModelProperty("媒体文件上传后获取的唯一标识")
    private String mediaId;
    /**
     * 媒体文件上传时间戳
     */
    @ApiModelProperty("媒体文件上传时间戳")
    private Long createdAt;
    /**
     * 上传后得到的图片URL。永久有效
     */
    @ApiModelProperty("图片路径")
    private String url;

    @ApiModelProperty("图片名称")
    private String fileName;


    private byte[] bytes;

    private InputStream inputStream;
}
