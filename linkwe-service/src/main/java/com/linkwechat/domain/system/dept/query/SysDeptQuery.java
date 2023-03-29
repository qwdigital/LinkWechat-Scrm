package com.linkwechat.domain.system.dept.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author danmo
 * @date 2022年11月30日 19:27
 */
@ApiModel
@Data
public class SysDeptQuery {

    @ApiModelProperty("部门ID")
    private List<Long> deptIds;
}
