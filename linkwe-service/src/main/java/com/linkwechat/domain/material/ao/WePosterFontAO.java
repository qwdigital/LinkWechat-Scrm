package com.linkwechat.domain.material.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WePosterFontAO {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 字体名称
     */
    @ApiModelProperty(value = "字体名称")
    private String fontName;

    /**
     * 字体网络地址
     */
    @ApiModelProperty(value = "字体链接")
    private String fontUrl;

    /**
     * 字体排序
     */
    @ApiModelProperty(value = "展示排序 顺序")
    private Integer order;

    @ApiModelProperty("资源类型")
    private String mediaType;

    @ApiModelProperty("分类id")
    private Long categoryId;
}
