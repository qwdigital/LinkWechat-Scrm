package com.linkwechat.domain.material.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.material.ao.WeMaterialImgAo;
import lombok.*;

import java.util.List;

/**
 * @description: 企业微信上传临时素材实体
 * @author: leejoker
 * @create: 2022-03-28 19:06:30
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("we_material")
public class WeMaterial extends BaseEntity {
//    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 模块类型，1素材中心，2企业话术，3客服话术
     */
    private Integer moduleType = 1;

    /**
     * 本地资源文件地址
     */
    private String materialUrl;


    /**
     * 素材标题
     */
    @Excel(name = "文本标题")
    private String materialName;

    /**
     * 文本内容、图片文案
     */
    @Excel(name = "文本内容")
    private String content;

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
     * 图片宽度
     */
    private Integer width;

    /**
     * 图片高度
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
     * 轨迹素材生成的H5链接
     */
    private String linkUrl;

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

    /**
     * 海报二维码组件类型：海报二维码组件类型：3占位码 4员工活码 5客群活码 6门店活码 7识客活码 8拉新活码
     */
    @TableField(value = "poster_qr_type")
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

    @TableField(exist = false)
    private List<WeMaterialImgAo> weMaterialImgAoList;


    /**
     * 标签id，多个使用逗号隔开
     */
    private String tagIds;

}
