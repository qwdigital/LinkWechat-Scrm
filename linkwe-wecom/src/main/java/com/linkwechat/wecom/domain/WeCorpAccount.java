package com.linkwechat.wecom.domain;

import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 企业id相关配置对象 wx_corp_account
 * 
 * @author ruoyi
 * @date 2020-08-24
 */
@Data
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


}
