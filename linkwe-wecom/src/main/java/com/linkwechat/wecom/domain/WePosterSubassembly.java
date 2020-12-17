package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 海报组成部件
 * @author ws
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "we_poster_subassembly")
public class WePosterSubassembly extends BaseEntity {

    @TableId
    private Long id;

    @TableField
    private Long posterId;

    /**
     * x坐标
     */
    @TableField(value = "`left`")
    private Integer left;

    /**
     * y坐标
     */
    @TableField
    private Integer top;

    /**
     * 宽度
     */
    @TableField
    private Integer width;

    /**
     * 高度
     */
    @TableField
    private Integer height;

    /**
     * 类型 1 固定文本 2 固定图片 3 二维码图片
     */
    @TableField
    private Integer type;

    @TableField
    private Long fontId;

    /**
     * 字体大小（像素大小）
     */
    @TableField
    private Integer fontSize;

    /**
     * 字体颜色（十六进制）
     */
    @TableField
    private String fontColor;

    /**
     * 字体对齐方式 0 左对齐 1居中 2右对齐
     */
    @TableField
    private Integer fontTextAlign;

    /**
     * 图片网络地址
     */
    @TableField
    private String imgPath;

    @TableField
    private Integer delFlag;

    /**
     * 文字内容
     */
    @TableField
    private String content;

    /**
     * 字体实体
     */
    @TableField(exist = false)
    private WePosterFont font;
}
