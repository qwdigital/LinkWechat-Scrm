package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.linkwechat.common.config.jackson.StringArrayDeserialize;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.wecom.domain.dto.WeUserDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;


/**
 * 通讯录相关客户对象 we_user
 * 
 * @author ruoyi
 * @date 2020-08-31
 */
@Data
@TableName("we_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeUser
{
    private static final long serialVersionUID = 1L;


    /** 用户头像 */
    @TableField(value = "head_image_url")
    private String avatarMediaid;


    /** 用户名称 */
    @NotBlank(message = "姓名不可为空")
    @TableField(value = "user_name")
    private String name;

    /** 用户昵称 */
    private String alias;

    /** 账号 */
    @NotBlank(message = "账号不可为空")
    @TableId
    private String userId;

    /** 性别。1表示男性，2表示女性 */
    private Integer gender;

    /** 手机号 */
    @NotBlank(message = "手机号不可为空")
    private String mobile;

    /** 邮箱 */
    private String email;

    /** 个人微信号 */
    private String wxAccount;

    /** 用户所属部门,使用逗号隔开,字符串格式存储 */
    private String[] department;

    /** 职务 */
    private String position;

    /** 1表示为上级,0表示普通成员(非上级)。 */
    private String[] isLeaderInDept;

    /** 入职时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date joinTime;

    /** 是否启用(1表示启用成员，0表示禁用成员) */
    private Integer enable;

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
    /** 激活状态: 1=已激活，2=已禁用，4=未激活，5=退出企业,6=删除 */
    private Integer isActivate;

    /** 离职是否分配(1:已分配;0:未分配;) */
    private Integer isAllocate;

    /** 离职时间 */
    private Date dimissionTime;


    private String remark;

    @TableField(exist = false)
    private String departmentStr;




    /** 转化成企业微信需要的dto对象 */
    public  WeUserDto transformWeUserDto(){
        WeUserDto weUserDto=new WeUserDto();

        BeanUtils.copyPropertiesASM(this,weUserDto);

        return weUserDto;
    }

    @JsonDeserialize(using = StringArrayDeserialize.class)
    public void setDepartment(String[] department) {
        this.department = department;
    }

    @JsonDeserialize(using = StringArrayDeserialize.class)
    public void setIsLeaderInDept(String[] isLeaderInDept) {
        this.isLeaderInDept = isLeaderInDept;
    }
}
