package com.linkwechat.wecom.domain;

import java.util.Date;
import java.util.List;

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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 企业微信客户对象 we_customer
 * 
 * @author ruoyi
 * @date 2020-09-13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_customer")
public class WeCustomer
{
    private static final long serialVersionUID = 1L;


    /** 外部联系人的userid */
    @TableId
    private String externalUserid;

    /** 外部联系人名称 */
    private String name;

    /** 外部联系人头像 */
    private String avatar;

    /** 外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户 */
    private Integer type;

    /** 外部联系人性别 0-未知 1-男性 2-女性 */
    private Integer gender;

    /** 外部联系人在微信开放平台的唯一身份标识,通过此字段企业可将外部联系人与公众号/小程序用户关联起来。 */
    private String unionid;

    /** 生日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;


    /** 客户企业简称 */
    private String corpName;

    /** 客户企业全称 */
    private String corpFullName;

    /** 职位 */
    private String  position;

    /** 添加人员 */
    @TableField(exist = false)
   private List<WeFlowerCustomerRel> weFlowerCustomerRels;





}
