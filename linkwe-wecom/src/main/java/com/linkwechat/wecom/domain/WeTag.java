package com.linkwechat.wecom.domain;

import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 企业微信标签对象 we_tag
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
public class WeTag extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 标签组id */
    @Excel(name = "标签组id")
    private Long groupId;

    /** 标签名 */
    @Excel(name = "标签名")
    private String name;

    /** 标签排序的次序值，order值大的排序靠前。 */
    @Excel(name = "标签排序的次序值，order值大的排序靠前。")
    private Long order;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setGroupId(Long groupId) 
    {
        this.groupId = groupId;
    }

    public Long getGroupId() 
    {
        return groupId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setOrder(Long order) 
    {
        this.order = order;
    }

    public Long getOrder() 
    {
        return order;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("groupId", getGroupId())
            .append("name", getName())
            .append("createTime", getCreateTime())
            .append("order", getOrder())
            .toString();
    }
}
