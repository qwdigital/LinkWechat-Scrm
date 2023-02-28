package com.linkwechat.domain.system.user.query;

import com.linkwechat.common.core.domain.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author danmo
 * @date 2022年11月29日 12:13
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUserQuery {

    @ApiModelProperty("企微员工ID")
    private List<String> weUserIds;

    @ApiModelProperty("部门ID")
    private List<Integer> deptIds;


    private List<SysUser> sysUsers;

}
