package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 客户标签关系对象 we_flower_customer_tag_rel
 * 
 * @author ruoyi
 * @date 2020-09-19
 */
@Data
@TableName("we_flower_customer_tag_rel")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeFlowerCustomerTagRel
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId
    private Long id;

    /** 添加客户的企业微信用户 */
    private Long flowerCustomerRelId;

    /** 标签id */
    private String tagId;

    /** 标签名 */
    @TableField(exist = false)
    private String tagName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
