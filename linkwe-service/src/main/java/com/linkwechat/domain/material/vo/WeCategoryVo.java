package com.linkwechat.domain.material.vo;

import com.linkwechat.common.core.domain.Tree;
import com.linkwechat.domain.material.entity.WeCategory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WeCategoryVo extends Tree<WeCategory> {


    @ApiModelProperty("素材数量")
    private Integer number = 0;

}
