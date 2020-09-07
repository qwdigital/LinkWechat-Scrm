package com.linkwechat.wecom.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 通讯录相关客户对象 we_user
 * 
 * @author ruoyi
 * @date 2020-08-31
 */
public class WeUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 头像地址 */
    private String headImageUrl;

    /** 用户名称 */
    private String userName;

    /** 用户昵称 */
    private String alias;

    /** 账号 */
    private String userId;

    /** 性别。1表示男性，2表示女性 */
    private Integer gender;

    /** 手机号 */
    private String mobile;

    /** 邮箱 */
    private String email;

    /** 个人微信号 */
    private String wxAccount;

    /** 用户所属部门,使用逗号隔开,字符串格式存储 */
    private String department;

    /** 职务 */
    private String position;

    /** 1表示为上级,0表示普通成员(非上级)。 */
    private Long isLeaderInDept;

    /** 入职时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date joinTime;

    /** 是否启用(1表示启用成员，0表示禁用成员) */
    private Long enable;

    /** 身份证号 */
    private String idCard;

    /** QQ号 */
    private String qqAccount;

    /** 座机 */
    private String telephone;

    /** 地址 */
    private String address;

    /** 生日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /** 是否激活（1:是；2:否）该字段主要表示当前信息是否同步微信 */
    private Long isActivate;

}
