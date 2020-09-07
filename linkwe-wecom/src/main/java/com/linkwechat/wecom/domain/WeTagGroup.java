package com.linkwechat.wecom.domain;

import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 标签组对象 we_tag_group
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
public class WeTagGroup extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 标签组id */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String gourpName;

    /** 标签组排序的次序值，order值大的排序靠前 */
    @Excel(name = "标签组排序的次序值，order值大的排序靠前")
    private Long order;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setGourpName(String gourpName) 
    {
        this.gourpName = gourpName;
    }

    public String getGourpName() 
    {
        return gourpName;
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
            .append("gourpName", getGourpName())
            .append("createTime", getCreateTime())
            .append("order", getOrder())
            .toString();
    }
}
