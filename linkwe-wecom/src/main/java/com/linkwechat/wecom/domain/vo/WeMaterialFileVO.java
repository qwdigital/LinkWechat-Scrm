package com.linkwechat.wecom.domain.vo;

import lombok.experimental.SuperBuilder;
import lombok.Data;

/**
 * 素材文件信息
 */
@SuperBuilder
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
