package com.linkwechat.domain.kf.query;

import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 运营入参
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeKfCustomerStatisticQuery extends BaseEntity {

    @ApiModelProperty("场景值")
    private List<String> scenes;

    @ApiModelProperty("客服账号ID")
    private List<String> openKfIds;

    @ApiModelProperty("接待人员ID")
    private List<String> userIds;
}
