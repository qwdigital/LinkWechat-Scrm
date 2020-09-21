package com.linkwechat.wecom.domain;

import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;


/**
 * 客户标签关系对象 we_flower_customer_tag_rel
 * 
 * @author ruoyi
 * @date 2020-09-19
 */
@Data
public class WeFlowerCustomerTagRel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 添加客户的企业微信用户 */
    private Long flowerCustomerRelId;

    /** 标签id */
    private Long tagId;

    /** 标签名 */
    private String tagName;


}
