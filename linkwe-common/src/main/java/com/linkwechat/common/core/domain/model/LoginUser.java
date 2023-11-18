package com.linkwechat.common.core.domain.model;

import com.linkwechat.common.core.domain.entity.SysUser;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 登录用户身份权限
 * 
 * @author ruoyi
 */
@Data
public class LoginUser implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 用户唯一标识 */
    private String token;

    /** 企业账号Id */
    private String corpId;

    /** 企业账号 */
    private String corpName;

    /** 租户标识 */
    private String isLessor;

    /** 用户名Id */
    private Long userId;

    /** 用户名 */
    private String userName;

    /** 用户标识 */
    private String userType;

    /** 登录时间 */
    private Long loginTime;

    /** 过期时间 */
    private Long expireTime;

    /** 登录IP地址 */
    private String ipaddr;

    /** 权限列表 */
    private Set<String> permissions;

    /** 角色权限列表 */
    private Set<String> roles;

    /** 角色列表 */
    private Set<Long> roleIds;

    /** 主数据源 */
    private String mainSource;

    /** 用户信息 */
    private SysUser sysUser;

    /**朋友圈同步需要的字段*/
    private Integer filterType;

    /**业务id，多个使用逗号隔开**/
    private String businessIds;

    /**企业微信员工id集合*/
    private List<String> weUserIds;

    /**企微客群id集合*/
    private List<String> chatIds;

    /**客户id集合*/
    private List<String> extIds;
}
