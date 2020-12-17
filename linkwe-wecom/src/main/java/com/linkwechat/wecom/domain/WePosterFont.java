package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 海报字体实体
 * @author ws
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "we_poster_font")
public class WePosterFont extends BaseEntity {

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 字体名称
     */
    @TableField(value = "font_name")
    private String fontName;

    /**
     * 字体网络地址
     */
    @TableField(value = "font_url")
    private String fontUrl;

    /**
     * 字体排序
     */
    @TableField(value = "`order`")
    private Integer order;
    /**
     * 状态
     */
    @TableField(value = "del_flag")
    private Integer delFlag;
}
