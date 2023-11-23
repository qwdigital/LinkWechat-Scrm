package com.linkwechat.domain.material.ao;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 海报
 *
 * @author ws
 */
@Data
@ApiModel(value = "海报")
@AllArgsConstructor
@NoArgsConstructor
public class WePoster {

    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 海报标题
     */
    @ApiModelProperty(value = "海报标题")
    private String materialName;

    /**
     * 海报标题
     */
    @Deprecated
    @ApiModelProperty(value = "海报标题")
    private String title;

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

    @ApiModelProperty(value = "模块类型")
    private Integer moduleType;

    @ApiModelProperty(value = "摘要")
    private String digest;

    /**
     * 轨迹素材生成的H5链接
     */
    @ApiModelProperty(value = "轨迹素材生成的H5链接")
    private String linkUrl;

    /**
     * 海报二维码组件类型：海报二维码组件类型：3占位码 4员工活码 5客群活码 6门店活码 7识客活码 8拉新活码
     */
    @ApiModelProperty(value = "海报二维码组件类型")
    private Integer posterQrType;

    /**
     * 像素大小
     */
    @TableField(value = "pixel_size")
    private Long pixelSize;

    /**
     * 内存大小
     */
    @TableField(value = "memory_size")
    private Long memorySize;

    /**
     * 海报组件数组
     */
    private List<WePosterSubassembly> posterSubassemblyList;


    /**
     * 标签id，多个使用逗号隔开
     */
    private String tagIds;
}
