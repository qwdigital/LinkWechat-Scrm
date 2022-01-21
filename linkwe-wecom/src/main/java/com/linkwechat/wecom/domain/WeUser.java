package com.linkwechat.wecom.domain;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.wecom.domain.dto.WeMediaDto;
import com.linkwechat.wecom.domain.dto.WeUserDto;
import com.linkwechat.wecom.service.IWeMaterialService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.util.Date;


/**
 * 通讯录相关客户对象 we_user
 * 
 * @author ruoyi
 * @date 2020-08-31
 */
@ApiModel
@Data
@TableName("we_user")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /** 用户头像 */
    @ApiModelProperty("用户头像")
    @TableField(value = "head_image_url")
    private String headImageUrl;


    /** 用户名称 */
    @ApiModelProperty("用户名称")
    @NotBlank(message = "姓名不可为空")
    @TableField(value = "user_name")
    private String name;

    /** 用户昵称 */
    @ApiModelProperty("用户昵称")
    private String alias;

    /** 账号 */
    @ApiModelProperty("账号")
    @NotBlank(message = "账号不可为空")
    @TableId
    private String userId;

    @ApiModelProperty("成员的open_userid")
    private String openUserId;

    /** 性别。1表示男性，2表示女性 */
    @ApiModelProperty("性别。1表示男性，2表示女性")
    private Integer gender;

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
    @ApiModelProperty("用户所属部门")
    private String department;


    /** 职务 */
    @ApiModelProperty("职务")
    private String position;

    /** 1表示为上级,0表示普通成员(非上级)。 */
    @ApiModelProperty("1表示为上级,0表示普通成员(非上级)")
    private String isLeaderInDept;

    /** 入职时间 */
    @ApiModelProperty("入职时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date joinTime;

    /** 是否启用(1表示启用成员，0表示禁用成员) */
    @ApiModelProperty("是否启用(1表示启用成员，0表示禁用成员)")
    private Integer enable;

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
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty("生日")
    private Date birthday;

    /** 是否激活（1:是；2:否）该字段主要表示当前信息是否同步微信 */
    /** 激活状态: 1=已激活，2=已禁用，4=未激活，5=退出企业*/
    @ApiModelProperty("激活状态: 1=已激活，2=已禁用，4=未激活，5=退出企业")
    private Integer isActivate=new Integer(4);

    /** 离职是否分配(1:已分配;0:未分配;) */
    @ApiModelProperty("离职是否分配(1:已分配;0:未分配;)")
    private Integer isAllocate;

    /** 离职时间 */
    @ApiModelProperty("离职时间")
    private Date dimissionTime;


    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否开启会话存档 0：关闭 1：开启")
    private Integer isOpenChat;


    @ApiModelProperty("是否配置客户联系人功能 1:是;0:否")
    private Integer isConfigCustomerContact;


    /** 转化成企业微信需要的dto对象 */
    public  WeUserDto transformWeUserDto(){
        WeUserDto weUserDto=new WeUserDto();
        BeanUtils.copyProperties(this,weUserDto);
        /**
         * 换取素材id
         */
        if(StringUtils.isNotEmpty(this.getHeadImageUrl())){
            WeMediaDto image = SpringUtils.getBean(IWeMaterialService.class)
                    .uploadTemporaryMaterial(this.getHeadImageUrl(), "image", FileUtil.getName(this.getHeadImageUrl()));
            if (image != null){
                weUserDto.setAvatar_mediaid(image.getMedia_id());
            }
        }
        weUserDto.setUserid(this.getUserId());
        if(StringUtils.isNotEmpty(this.getIsLeaderInDept())){
            weUserDto.setIs_leader_in_dept(CollectionUtil.toList(this.getIsLeaderInDept().split(",")));
        }
        if(StringUtils.isNotEmpty(this.getDepartment())){
            weUserDto.setDepartment(CollectionUtil.toList(this.getDepartment().split(",")));
        }
        return weUserDto;
    }
}
