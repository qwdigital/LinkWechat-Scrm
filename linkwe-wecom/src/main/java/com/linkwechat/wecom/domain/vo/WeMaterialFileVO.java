package com.linkwechat.wecom.domain.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 素材文件信息
 */
@Builder
@Data
public class WeMaterialFileVO {

    /**
     * 素材名称
     */
    private String materialName;

    /**
     * 本地资源文件地址
     */
    private String materialUrl;

    /**
     * 媒体id
     */
    private String materialMediaId;

    /**
     * 媒体文件上传时间戳
     */
    private Long materialCreatedAt;

}
