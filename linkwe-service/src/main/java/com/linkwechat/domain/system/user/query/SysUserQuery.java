package com.linkwechat.domain.system.user.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author danmo
 * @date 2022年11月29日 12:13
 */
@ApiModel
@Data
public class SysUserQuery {

    @ApiModelProperty("企微员工ID")
    private List<String> openUserIds;

    @ApiModelProperty("部门ID")
    private List<Integer> deptIds;

}
