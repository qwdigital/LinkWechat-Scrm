package com.linkwechat.domain.material.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * 素材文件信息
 */
@Builder
@Data
public class WeMaterialFileVo {

    /**
     * 素材名称
     */
    private String materialName;

    /**
     * 本地资源文件地址
     */
    private String materialUrl;

}