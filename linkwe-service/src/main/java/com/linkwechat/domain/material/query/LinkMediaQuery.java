package com.linkwechat.domain.material.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author sxw
 * @description 素材入参
 * @date 2022/4/30 18:25
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkMediaQuery extends BaseEntity {

    @ApiModelProperty("分类ID")
    private String categoryId;

    @ApiModelProperty("素材查询")
    private String search;

    @ApiModelProperty("素材类型 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本 5 海报 6 海报字体")
    private String mediaType;

    @ApiModelProperty("状态 0-启用 1-不启用")
    private Integer status;

}
