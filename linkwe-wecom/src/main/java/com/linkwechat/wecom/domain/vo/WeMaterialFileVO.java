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

}
