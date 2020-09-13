package com.linkwechat.wecom.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 企业微信客户对象 we_customer
 * 
 * @author ruoyi
 * @date 2020-09-13
 */
public class WeCustomer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 外部联系人的userid */
    private String externalUserid;

    /** 外部联系人名称 */
    private String name;

    /** 外部联系人头像 */
    private String avatar;

    /** 外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户 */
    private Long type;

    /** 外部联系人性别 0-未知 1-男性 2-女性 */
    private Long gender;

    /** 外部联系人在微信开放平台的唯一身份标识,通过此字段企业可将外部联系人与公众号/小程序用户关联起来。 */
    private String unionid;

    /** 生日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /** 添加人员id */
    private String userId;

    /** 添加人员名称 */
    private String userName;

    /** 所在部门名称 */
    private String departmentName;

    /** 描述 */
    private String description;

    /** 客户备注的手机号码 */
    private String remarkMobiles;

    /** 客户备注的企业名称 */
    private String remarkCorpName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setExternalUserid(String externalUserid) 
    {
        this.externalUserid = externalUserid;
    }

    public String getExternalUserid() 
    {
        return externalUserid;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setAvatar(String avatar) 
    {
        this.avatar = avatar;
    }

    public String getAvatar() 
    {
        return avatar;
    }
    public void setType(Long type) 
    {
        this.type = type;
    }

    public Long getType() 
    {
        return type;
    }
    public void setGender(Long gender) 
    {
        this.gender = gender;
    }

    public Long getGender() 
    {
        return gender;
    }
    public void setUnionid(String unionid) 
    {
        this.unionid = unionid;
    }

    public String getUnionid() 
    {
        return unionid;
    }
    public void setBirthday(Date birthday) 
    {
        this.birthday = birthday;
    }

    public Date getBirthday() 
    {
        return birthday;
    }
    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setDepartmentName(String departmentName) 
    {
        this.departmentName = departmentName;
    }

    public String getDepartmentName() 
    {
        return departmentName;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setRemarkMobiles(String remarkMobiles) 
    {
        this.remarkMobiles = remarkMobiles;
    }

    public String getRemarkMobiles() 
    {
        return remarkMobiles;
    }
    public void setRemarkCorpName(String remarkCorpName) 
    {
        this.remarkCorpName = remarkCorpName;
    }

    public String getRemarkCorpName() 
    {
        return remarkCorpName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("externalUserid", getExternalUserid())
            .append("name", getName())
            .append("avatar", getAvatar())
            .append("type", getType())
            .append("gender", getGender())
            .append("unionid", getUnionid())
            .append("birthday", getBirthday())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("createTime", getCreateTime())
            .append("departmentName", getDepartmentName())
            .append("description", getDescription())
            .append("remarkMobiles", getRemarkMobiles())
            .append("remarkCorpName", getRemarkCorpName())
            .toString();
    }
}
