package com.linkwechat.domain.material.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @description: 企业微信上传临时素材实体
 * @author: leejoker
 * @create: 2022-03-28 19:06:30
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("we_material")
public class WeMaterial extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 本地资源文件地址
     */
    private String materialUrl;

    /**
     * 文本内容、图片文案
     */
    private String content;

    /**
     * 素材标题
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

    /**
     * 音频时长
     */
    private String audioTime;


    /**
     * 背景图片
     */
    private String backgroundImgUrl;

    /**
     * 海报类型 1 通用海报
     */
    private Long type;

    /**
     * 海报背景宽度
     */
    private Integer width;

    /**
     * 海报背景高度
     */
    private Integer height;

    /**
     * 资源类型
     */
    private String mediaType;


    /**
     * 其他字段
     */
    private String otherField;


    /**
     * 状态 0-启用 1-不启用
     */
    private Integer materialStatus;

    /**
     * 字体排序
     */
    private Integer frontOrder;

    /**
     * 状态
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 素材是否被选中
     */
    @TableField(exist = false)
    private Boolean isCheck = false;

    /**
     * 海报组件数组
     */
    @TableField(value = "subassembly")
    private String posterSubassembly;
}
