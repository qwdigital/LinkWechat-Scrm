package com.linkwechat.domain.material.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * 素材文件信息
 */
@ApiModel
@SuperBuilder
@Data
public class WeMaterialFileVo {

    /**
     * 素材名称
     */
    @ApiModelProperty("素材名称")
    private String materialName;

    /**
     * 本地资源文件地址
     */
    @ApiModelProperty("本地资源文件地址")
    private String materialUrl;


    /**
     * 媒体文件上传后获取的唯一标识，3天内有效
     */
    @ApiModelProperty("媒体ID")
    private String mediaId;

    /**
     * 媒体文件上传时间戳
     */
    @ApiModelProperty("媒体文件上传时间戳")
    private Long createdAt;
}