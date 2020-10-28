package com.linkwechat.wecom.domain;

import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.Data;

/**
 * @description: 企业微信上传临时素材实体
 * @author: KEWEN
 * @create: 2020-09-21 21:17
 **/
@Data
public class WeMaterial extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id = SnowFlakeUtil.nextId();

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 本地资源文件地址
     */
    private String materialUrl;

    /**
     * 文本内容、图片文案
     */
    private String content;

    /**
     * 图片名称
     */
    private String materialName;

    /**
     * 摘要
     */
    private String digest;

    /**
     * 封面本地资源文件
     */
    private String coverUrl;

}
