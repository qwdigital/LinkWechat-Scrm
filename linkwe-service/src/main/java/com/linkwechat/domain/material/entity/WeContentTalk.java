package com.linkwechat.domain.material.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("we_content_talk")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "话术中心表")
public class WeContentTalk extends BaseEntity {
    @TableId
    private Long id;

    private Long categoryId;

    @TableField(exist = false)
    private String categoryName;

    private String talkTitle;

    private Integer talkType = 0;

    @TableField(exist = false)
    private List<Long> materialIdList;

    @TableField(exist = false)
    private List<WeMaterial> weMaterialList;

    private Integer delFlag;

    @TableField(exist = false)
    private List<WeTalkMaterial> talkMaterialList;
}
