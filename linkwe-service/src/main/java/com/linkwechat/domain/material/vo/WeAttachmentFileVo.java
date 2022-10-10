package com.linkwechat.domain.material.vo;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * 附件文件信息
 */
@SuperBuilder
@Data
public class WeAttachmentFileVo extends WeMaterialFileVo{

    /**
     * 素材名称
     */
    private String mediaId;

}