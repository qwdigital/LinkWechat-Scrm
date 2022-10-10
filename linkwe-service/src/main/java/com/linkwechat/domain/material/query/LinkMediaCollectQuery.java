package com.linkwechat.domain.material.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 新增素材收藏
 */
@ApiModel
@Data
public class LinkMediaCollectQuery {

    @ApiModelProperty("素材ID")
    private Long materialId;

    @ApiModelProperty("员工ID")
    private String userId;

}
