package com.linkwechat.wecom.domain;

import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.Data;

@Data
public class WeCategory extends BaseEntity {

    private Long id = SnowFlakeUtil.nextId();

    /**
     * 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本
     */
    private String mediaType;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父分类的id
     */
    private Long parentId;

}
