package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 海报组成部件
 * @author ws
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "we_poster_subassembly")
@ApiModel(value = "海报组成部件")
public class WePosterSubassembly extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    @TableField
    @ApiModelProperty(value = "海报id")
    private Long posterId;

    /**
     * x坐标
     */
    @TableField(value = "`left`")
    @ApiModelProperty(value = "x坐标")
    private Integer left;

    /**
     * y坐标
     */
    @TableField
    @ApiModelProperty(value = "y坐标")
    private Integer top;

    /**
     * 宽度
     */
    @TableField
    @ApiModelProperty(value = "控件宽度")
    private Integer width;

    /**
     * 高度
     */
    @TableField
    @ApiModelProperty(value = "空间高度")
    private Integer height;

    /**
     * 类型 1 固定文本 2 固定图片 3 二维码图片
     */
    @TableField
    @ApiModelProperty(value = "类型 1 固定文本 2 固定图片 3 二维码图片")
    private Integer type;

    @TableField
    @ApiModelProperty(value = "字体id")
    private Long fontId;

    /**
     * 字体大小（像素大小）
     */
    @TableField
    @ApiModelProperty(value = "字体大小（像素大小）")
    private Integer fontSize;

    /**
     * 字体颜色（十六进制）
     */
    @TableField
    @ApiModelProperty(value = "字体颜色（十六进制）")
    private String fontColor;

    /**
     * 字体对齐方式 0 左对齐 1居中 2右对齐
     */
    @TableField
    @ApiModelProperty(value = "字体对齐方式 1 左对齐 2 居中 3右对齐")
    private Integer fontTextAlign = 2;

    /**
     * 字体垂直对齐方式 0 上对齐 1居中 2下对齐
     */
    @TableField
    @ApiModelProperty(value = "字体垂直对齐方式 1 上对齐 2 居中 3 下对齐")
    private Integer verticalType = 2;

    /**
     * 图片网络地址
     */
    @TableField
    @ApiModelProperty(value = "图片地址（与字体id互斥）")
    private String imgPath;

    @TableField
    @ApiModelProperty(value = "0 启用 1 删除")
    private Integer delFlag;

    /**
     * 文字内容
     */
    @TableField
    @ApiModelProperty(value = "文本内容")
    private String content;

    @TableField
    @ApiModelProperty("字间距")
    private Integer wordSpace = 0;

    @TableField
    @ApiModelProperty("行间距")
    private Integer lineSpace = 0;

    @TableField
    @ApiModelProperty("透明度[0,255]")
    private Integer alpha;

    @TableField
    @ApiModelProperty("旋转角度（顺时针）")
    private Integer rotate;

    @TableField
    @ApiModelProperty("字体类型 0 通常 1 粗体 2 斜体 3 粗体+斜体")
    private Integer fontStyle = 0;

    @TableField(value = "`order`")
    @ApiModelProperty("顺序排序")
    private Integer order;
    /**
     * 字体实体
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "字体信息")
    private WePosterFont font;
}
