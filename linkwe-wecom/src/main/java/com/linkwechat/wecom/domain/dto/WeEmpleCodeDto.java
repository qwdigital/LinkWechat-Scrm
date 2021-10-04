package com.linkwechat.wecom.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeEmpleCodeDto {
    /**
     * 活码id
     */
    private String empleCodeId;
    /**
     * 欢迎语
     */
    private String welcomeMsg;
    /**
     * 分类id
     */
    private String categoryId;
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
