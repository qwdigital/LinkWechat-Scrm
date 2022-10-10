package com.linkwechat.domain.operation.query;

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
public class WeOperationCustomerQuery extends BaseEntity {

    @ApiModelProperty("员工")
    private List<String> userIds;

    @ApiModelProperty("部门")
    private List<Integer> deptIds;


}
