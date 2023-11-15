package com.linkwechat.domain.material.vo;

import com.alibaba.fastjson.JSONArray;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WePosterVo extends BaseEntity {
    private Long id;

    /**
     * 海报标题
     */
    @Deprecated
    @ApiModelProperty(value = "海报标题")
    private String title;

    /**
     * 海报标题
     */
    @ApiModelProperty(value = "海报标题")
    private String materialName;

    /**
     * 海报描述
     */
    @ApiModelProperty(value = "海报描述")
    private String digest;

    /**
     * 背景图片
     */
    @ApiModelProperty(value = "背景图片")
    private String backgroundImgPath;

    /**
     * 示例图片
     */
    @ApiModelProperty(value = "示例图片")
    private String sampleImgPath;

    /**
     * 海报类型 1 通用海报
     */
    @ApiModelProperty(value = "海报类型 1 通用海报（默认）")
    private Long type;

    @ApiModelProperty(value = "海报背景宽度")
    private Integer width;

    @ApiModelProperty(value = "海报背景高度")
    private Integer height;

    @ApiModelProperty("资源类型")
    private String mediaType;

    @ApiModelProperty("分类id")
    private Long categoryId;


    @ApiModelProperty("其他字段")
    private String otherField;

    @ApiModelProperty("状态 0-启用 1-不启用")
    private Integer status;

    @ApiModelProperty(value = "删除状态 0-正常 1-删除")
    private Integer delFlag;

    /**
     * 轨迹素材生成的H5链接
     */
    @ApiModelProperty(value = "轨迹素材生成的H5链接")
    private String linkUrl;

    /**
     * 海报组件数组
     */
    private JSONArray posterSubassemblyList;


    /**
     * 标签id，多个使用逗号隔开
     */
    private String tagIds;

    /**
     * 标签名
     */
    private String tagNames;
}
