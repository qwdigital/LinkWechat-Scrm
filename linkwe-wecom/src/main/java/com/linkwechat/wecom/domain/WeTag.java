package com.linkwechat.wecom.domain;

import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 企业微信标签对象 we_tag
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
@Data
public class WeTag extends BaseEntity
{
    private static final long serialVersionUID = 1L;



    /** 标签组id */
    @Excel(name = "标签组id")
    private String groupId;

    /** 标签名 */
    @Excel(name = "标签名")
    private String name;



}
