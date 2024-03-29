package com.linkwechat.domain.material.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("we_category")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "素材分类")
public class WeCategory extends BaseEntity {

    @TableId
    @ApiModelProperty("素材主键")
    private Long id;


    @ApiModelProperty("素材分类：0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本 5 海报 6 活码")
    private String mediaType;


    @ApiModelProperty("分类名称")
    @NotEmpty(message = "分类名称不能为空")
    private String name;


    @ApiModelProperty("素材上级父节点id")
    private Long parentId = 0L;

    @ApiModelProperty("可删除标识 0 可删除 1 不可删除")
    private Integer flag;

    @ApiModelProperty("素材状态:0 未删除 2 已删除")
    @TableLogic
    private String delFlag;
}
