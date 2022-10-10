package com.linkwechat.web.domain.vo;

import lombok.Data;

/**
 * @author leejoker
 * @date 2022/4/27 1:50
 */
@Data
public class UserRoleVo {
    private Long userId;
    private Long roleId;
    private String roleName;
}
