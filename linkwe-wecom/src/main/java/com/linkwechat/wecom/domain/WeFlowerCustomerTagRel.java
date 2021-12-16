package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
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
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeFlowerCustomerTagRel
{
    private static final long serialVersionUID = 1L;




    /**外部联系人id(客户id) */
    private String externalUserid;

    /** 标签id */
    private String tagId;

    /** 标签名 */
    @TableField(exist = false)
    private String tagName;

//    /**0:移动端打的标签;1:pc端打的标签**/
//    private Integer relTagType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String userId;

    @TableLogic
    private Integer delFlag;

    //是否是企业标签:1:是;0:否
    private Boolean isCompanyTag;
}
