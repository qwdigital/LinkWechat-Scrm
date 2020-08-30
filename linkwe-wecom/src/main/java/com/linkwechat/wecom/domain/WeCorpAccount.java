package com.linkwechat.wecom.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;

/**
 * 企业id相关配置对象 wx_corp_account
 * 
 * @author ruoyi
 * @date 2020-08-24
 */
public class WeCorpAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 企业名称 */
    @Excel(name = "企业名称")
    private String companyName;

    /** 企业ID */
    @Excel(name = "企业ID")
    private String corpId;

    /** 应用的密钥凭证 */
    @Excel(name = "应用的密钥凭证")
    private String corpSecret;

    /** 帐号状态（0正常 1停用) */
    @Excel(name = "帐号状态", readConverterExp = "帐号状态（0正常 1停用)")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCompanyName(String companyName) 
    {
        this.companyName = companyName;
    }

    public String getCompanyName() 
    {
        return companyName;
    }
    public void setCorpId(String corpId) 
    {
        this.corpId = corpId;
    }

    public String getCorpId() 
    {
        return corpId;
    }
    public void setCorpSecret(String corpSecret) 
    {
        this.corpSecret = corpSecret;
    }

    public String getCorpSecret() 
    {
        return corpSecret;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("companyName", getCompanyName())
            .append("corpId", getCorpId())
            .append("corpSecret", getCorpSecret())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
