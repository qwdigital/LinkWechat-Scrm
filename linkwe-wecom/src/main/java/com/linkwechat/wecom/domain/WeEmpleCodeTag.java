package com.linkwechat.wecom.domain;

import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.Data;


/**
 * 员工活码标签对象 we_emple_code_tag
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
@Data
public class WeEmpleCodeTag extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id= SnowFlakeUtil.nextId();

    /** 标签id */
    private Long tagId;

    /** 员工活码id */
    private Long empleCodeId;

    /** 0:正常;2:删除; */
    private Integer delFlag=new Integer(0);

    /** 标签名 */
    private String tagName;


}
