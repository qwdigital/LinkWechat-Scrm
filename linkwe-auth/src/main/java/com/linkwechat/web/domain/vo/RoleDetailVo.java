package com.linkwechat.web.domain.vo;

import com.linkwechat.common.core.domain.entity.SysDept;
import com.linkwechat.common.core.domain.entity.SysMenu;
import lombok.Data;

import java.util.List;

/**
 * @author leejoker
 * @version 1.0
 * @date 2022/4/19 13:22
 */
@Data
public class RoleDetailVo {
    private Long roleId;
    private String roleName;
    private String dataScope;
    private String roleKey;
    private List<RoleUserVo> users;
    private List<SysDept> depts;
    private List<SysMenu> menus;
    private String roleSort;
}
