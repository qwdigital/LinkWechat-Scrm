package com.linkwechat.wecom.domain;


import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 客户标签关系对象 we_customer_tag_rel
 * 
 * @author ruoyi
 * @date 2020-09-13
 */
public class WeCustomerTagRel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id= SnowFlakeUtil.nextId();;

    /** 客户id */
    private Long customerId;

    /** 标签id */
    private Long tagId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCustomerId(Long customerId) 
    {
        this.customerId = customerId;
    }

    public Long getCustomerId() 
    {
        return customerId;
    }
    public void setTagId(Long tagId) 
    {
        this.tagId = tagId;
    }

    public Long getTagId() 
    {
        return tagId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("customerId", getCustomerId())
            .append("tagId", getTagId())
            .append("createTime", getCreateTime())
            .toString();
    }
}
