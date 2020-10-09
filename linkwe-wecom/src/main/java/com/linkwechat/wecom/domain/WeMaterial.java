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
     * 媒体id
     */
    private String materialMediaId;

    /**
     * 媒体文件上传时间戳
     */
    private String materialCreatedAt;

    /**
     * 视频封面媒体id
     */
    private String coverMediaId;

    /**
     * 视频封面媒体文件上传时间戳
     */
    private String coverCreatedAt;

    /**
     * 封面本地资源文件
     */
    private String coverUrl;

}
