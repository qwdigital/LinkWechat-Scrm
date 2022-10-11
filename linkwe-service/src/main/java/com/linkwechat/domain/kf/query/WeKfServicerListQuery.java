package com.linkwechat.domain.kf.query;

import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 客服接待人员列表入参
 * @date 2022/1/18 21:48
 **/
@ApiModel
@Data
public class WeKfServicerListQuery extends BaseEntity {

    @ApiModelProperty("客服主键ID")
    private Long kfId;

    @ApiModelProperty("客服ID")
    private String openKfId;

    @ApiModelProperty("接待人员的接待状态。0:接待中,1:停止接待")
    private Integer status;

    @ApiModelProperty("接待员工id")
    private List<String> userIds;
}
