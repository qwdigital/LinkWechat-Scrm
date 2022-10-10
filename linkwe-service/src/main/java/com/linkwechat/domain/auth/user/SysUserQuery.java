package com.linkwechat.domain.auth.user;

import com.linkwechat.common.core.domain.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @author danmo
 * @date 2022年09月26日 14:36
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class SysUserQuery extends SysUser {

    @ApiModelProperty("员工ID")
    private List<Long> userIds;

    @ApiModelProperty("员工企微ID")
    private List<String> weUserIds;
}
