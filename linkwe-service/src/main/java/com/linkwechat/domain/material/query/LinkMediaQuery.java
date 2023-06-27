package com.linkwechat.domain.material.query;

import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author danmo
 * @description 素材入参
 * @date 2022/4/30 18:25
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkMediaQuery extends BaseEntity {

    /**
     * 传这个表示查询该id对象的信息详情
     */
    private Long materialId;

    @ApiModelProperty("分类ID")
    private String categoryId;

    @ApiModelProperty("素材查询")
    private String search;

    @ApiModelProperty("素材标题")
    private String materialName;

    /**
     * 素材类型
     *
     * @see com.linkwechat.common.enums.CategoryMediaType
     */
    @ApiModelProperty("素材类型 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本 5 海报 6 海报字体")
    private String mediaType;

    @ApiModelProperty("状态 0-启用 1-不启用")
    private Integer status;

    private List<String> mediaTypeNoList;

    List<Long> moduleTypeSonList = new ArrayList<>(Arrays.asList(1L));

    Integer resourceType = 1;

    @ApiModelProperty("海报类型：1通用海报，2裂变海报")
    private Integer type;

    /**
     * 应用场景：1朋友圈
     * 默认值 -1
     */
    private Integer scene = -1;

    /**
     * 像素大小
     */
    private Long pixelSize;

    /**
     * 内存大小
     */
    private Long memorySize;
}
