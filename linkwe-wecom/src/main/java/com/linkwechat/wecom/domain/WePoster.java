package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 海报
 *
 * @author ws
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "we_poster")
@ApiModel(value = "海报")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WePoster extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 海报标题
     */
    @TableField
    @ApiModelProperty(value = "海报标题")
    private String title;

    /**
     * 背景图片
     */
    @TableField
    @ApiModelProperty(value = "背景图片")
    private String backgroundImgPath;

    /**
     * 示例图片
     */
    @TableField
    @ApiModelProperty(value = "示例图片")
    private String sampleImgPath;

    /**
     * 海报类型 1 通用海报
     */
    @TableField
    @ApiModelProperty(value = "海报类型 1 通用海报（默认）")
    private Long type;

    @TableField
    @ApiModelProperty(value = "海报背景宽度")
    private Integer width;

    @TableField
    @ApiModelProperty(value = "海报背景高度")
    private Integer height;

    @TableField
    @ApiModelProperty("资源类型")
    private String mediaType;

    @TableField
    @ApiModelProperty("分类id")
    private Long categoryId;


    @ApiModelProperty("其他字段")
    private String otherField;

    @ApiModelProperty("状态 0-启用 1-不启用")
    private Integer status;

    @ApiModelProperty(value = "删除状态 0-正常 1-删除")
    private Integer delFlag;
    /**
     * 海报组件数组
     */
    @TableField(exist = false)
    private List<WePosterSubassembly> posterSubassemblyList;


}
