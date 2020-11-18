package com.linkwechat.wecom.domain.dto;

import lombok.Data;

@Data
public class WeEmpleCodeDto {
    /**
     * 欢迎语
     */
    private String welcomeMsg;
    /**
     * 欢迎语模板类型:1:员工欢迎语;2:部门员工欢迎语;3:客户群欢迎语
     */
    private Integer welcomeMsgTplType;
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
