package com.linkwechat.wecom.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 聊天侧边栏
 * @author kewen
 */
@Data
public class WeChatSideVo {

    /**
     *  0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本 5 海报
     */
    private String mediaType;

    /**
     * 素材id
     */
    private Long materialId;

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
     * 是否收藏 0未收藏 1 已收藏
     */
    private String collection;


    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 封面地址
     */
    private String coverUrl;

}
