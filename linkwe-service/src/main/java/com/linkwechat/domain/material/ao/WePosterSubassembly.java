package com.linkwechat.domain.material.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 海报组成部件
 *
 * @author ws
 */
@Data
@ApiModel(value = "海报组成部件")
public class WePosterSubassembly {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "海报id")
    private Long posterId;

    /**
     * x坐标
     */
    @ApiModelProperty(value = "x坐标")
    private Integer left;

    /**
     * y坐标
     */
    @ApiModelProperty(value = "y坐标")
    private Integer top;

    /**
     * 宽度
     */
    @ApiModelProperty(value = "控件宽度")
    private Integer width;

    /**
     * 高度
     */
    @ApiModelProperty(value = "空间高度")
    private Integer height;

    /**
     * 类型 1 固定文本 2 固定图片 3 二维码图片
     */
    @ApiModelProperty(value = "类型 1 固定文本 2 固定图片 3 二维码图片")
    private Integer type;

    @ApiModelProperty(value = "字体id")
    private Long fontId;

    /**
     * 字体大小（像素大小）
     */
    @ApiModelProperty(value = "字体大小（像素大小）")
    private Integer fontSize;

    /**
     * 字体颜色（十六进制）
     */
    @ApiModelProperty(value = "字体颜色（十六进制）")
    private String fontColor;

    /**
     * 字体对齐方式 0 左对齐 1居中 2右对齐
     */

    @ApiModelProperty(value = "字体对齐方式 1 左对齐 2 居中 3右对齐")
    private Integer fontTextAlign = 2;

    /**
     * 字体垂直对齐方式 0 上对齐 1居中 2下对齐
     */

    @ApiModelProperty(value = "字体垂直对齐方式 1 上对齐 2 居中 3 下对齐")
    private Integer verticalType = 2;

    /**
     * 图片网络地址
     */

    @ApiModelProperty(value = "图片地址（与字体id互斥）")
    private String imgPath;


    @ApiModelProperty(value = "0 启用 1 删除")
    private Integer delFlag;

    /**
     * 文字内容
     */

    @ApiModelProperty(value = "文本内容")
    private String content;


    @ApiModelProperty("字间距")
    private Integer wordSpace = 0;


    @ApiModelProperty("行间距")
    private Integer lineSpace = 0;


    @ApiModelProperty("透明度[0,255]")
    private Integer alpha;


    @ApiModelProperty("旋转角度（顺时针）")
    private Integer rotate;


    @ApiModelProperty("字体类型 0 通常 1 粗体 2 斜体 3 粗体+斜体")
    private Integer fontStyle = 0;

    @ApiModelProperty("顺序排序")
    private Integer order;
    /**
     * 字体实体
     */
    @ApiModelProperty(value = "字体信息")
    private WePosterFontAO font;
}