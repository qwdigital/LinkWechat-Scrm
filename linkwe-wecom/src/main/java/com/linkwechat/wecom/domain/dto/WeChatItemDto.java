package com.linkwechat.wecom.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 侧边栏素材
 *
 * @author kewen
 */
@Data
public class WeChatItemDto {

    /**
     * 素材id列表
     */
    private List<Long> materialIds;

    /**
     * 侧边栏id
     */
    private Long sideId;

    /**
     * 素材类型 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本 5 海报
     */
    private String mediaType;

    /**
     * 是否全选 0 全选 1 非全选
     */
    private String checkAll;

}
