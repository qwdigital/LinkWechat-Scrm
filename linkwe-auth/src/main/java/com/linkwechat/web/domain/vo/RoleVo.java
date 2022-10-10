package com.linkwechat.web.domain.vo;

import com.dtflys.forest.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.enums.RoleType;
import lombok.Data;

import java.util.Date;

/**
 * @author leejoker
 * @version 1.0
 * @date 2022/4/19 12:49
 */
@Data
public class RoleVo {
    private Long roleId;

    private String roleName;

    private String roleKey;

    private String roleSort;

    private String status;

    private String delFlag;

    private String remark;

    private String dataScope;

     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Integer userCount;

    private boolean isBase;

    private boolean isSuperAdmin;

    public void setRoleKey(String roleKey) {
        if (StringUtils.isNotBlank(roleKey) && RoleType.baseRoleKeys().contains(roleKey)) {
            this.isBase = true;
            if (RoleType.WECOME_USER_TYPE_WBXTGLY.getSysRoleKey().equals(roleKey)) {
                this.isSuperAdmin = true;
            }
        }
        this.roleKey = roleKey;
    }
}
