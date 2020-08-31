package com.linkwechat.wecom.domain;


import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 企业微信组织架构相关对象 we_department
 * 
 * @author ruoyi
 * @date 2020-09-01
 */
@Data
public class WeDepartment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String name;

    /** 部门英文名称 */
    @Excel(name = "部门英文名称")
    private String nameEn;

    /** $column.columnComment */
    @Excel(name = "部门英文名称")
    private Long parentId;

    /** 是否激活（1:是；2:否）该字段主要表示当前信息是否同步微信 */
    @Excel(name = "是否激活", readConverterExp = "1=:是；2:否")
    private Long isActivate;

    /** 在父部门中的次序值。order值大的排序靠前。 */
    @Excel(name = "在父部门中的次序值。order值大的排序靠前。")
    private Long order;


}
