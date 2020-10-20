package com.linkwechat.wecom.domain;

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
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;


/**
 * 通讯录相关客户对象 we_user
 * 
 * @author ruoyi
 * @date 2020-08-31
 */
@Data
@ApiModel("通讯录用户")
@TableName("we_user")
public class WeUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id= SnowFlakeUtil.nextId();

    /** 用户头像 */
    @ApiModelProperty("用户头像,该值来自素材库")
    private String avatarMediaid;

    /** 用户名称 */
    @ApiModelProperty("姓名")
    @NotBlank(message = "姓名不可为空")
    private String name;

    /** 用户昵称 */
    @ApiModelProperty("昵称")
    private String alias;

    /** 账号 */
    @ApiModelProperty("账号")
    @NotBlank(message = "账号不可为空")
    private String userId;

    /** 性别。1表示男性，2表示女性 */
    @ApiModelProperty("性别。1表示男性，2表示女性")
    private Integer gender=new Integer(1);

    /** 手机号 */
    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不可为空")
    private String mobile;

    /** 邮箱 */
    @ApiModelProperty("邮箱")
    private String email;

    /** 个人微信号 */
    @ApiModelProperty("个人微信号")
    private String wxAccount;

    /** 用户所属部门,使用逗号隔开,字符串格式存储 */
    @ApiModelProperty("用户所属部门,使用逗号隔开,字符串格式存储")
    private String[] department;

    /** 职务 */
    @ApiModelProperty("职务")
    private String position;

    /** 1表示为上级,0表示普通成员(非上级)。 */
    @ApiModelProperty("1表示为上级,0表示普通成员(非上级)")
    private String[] isLeaderInDept;

    /** 入职时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("入职时间")
    private Date joinTime;

    /** 是否启用(1表示启用成员，0表示禁用成员) */
    @ApiModelProperty("是否启用(1表示启用成员，0表示禁用成员)")
    private Integer enable=new Integer(1);

    /** 身份证号 */
    @ApiModelProperty("身份证号")
    private String idCard;

    /** QQ号 */
    @ApiModelProperty("QQ号")
    private String qqAccount;

    /** 座机 */
    @ApiModelProperty("座机")
    private String telephone;

    /** 地址 */
    @ApiModelProperty("地址")
    private String address;

    /** 生日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("生日")
    private Date birthday;

    /** 是否激活（1:是；2:否）该字段主要表示当前信息是否同步微信 */
    @ApiModelProperty("是否激活（1:是；2:否）该字段主要表示当前信息是否同步微信 ")
    private Integer isActivate=new Integer(2);



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
