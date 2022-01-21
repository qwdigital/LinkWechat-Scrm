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
 * 海报字体实体
 * @author ws
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "we_poster_font")
@ApiModel(value = "海报字体")
public class WePosterFont extends BaseEntity {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 字体名称
     */
    @TableField(value = "font_name")
    @ApiModelProperty(value = "字体名称")
    private String fontName;

    /**
     * 字体网络地址
     */
    @TableField(value = "font_url")
    @ApiModelProperty(value = "字体链接")
    private String fontUrl;

    /**
     * 字体排序
     */
    @TableField(value = "`order`")
    @ApiModelProperty(value = "展示排序 顺序")
    private Integer order;
    /**
     * 状态
     */
    @TableField(value = "del_flag")
    @ApiModelProperty(value = "0 启用 1 关闭")
    private Integer delFlag;

    @TableField
    @ApiModelProperty("资源类型")
    private String mediaType;

    @TableField
    @ApiModelProperty("分类id")
    private Long categoryId;
}
