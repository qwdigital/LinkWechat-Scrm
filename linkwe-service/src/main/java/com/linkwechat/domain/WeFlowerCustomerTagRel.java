package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("we_flower_customer_tag_rel")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeFlowerCustomerTagRel extends BaseEntity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 客户id
     */
    private String externalUserid;

    /**
     * 标签id
     */
    private String tagId;

    /**
     * 员工id
     */
    private String userId;

    /**
     * 是否是企业标签:1:是企业标签;0:个人标签
     */
    private Boolean isCompanyTag;

    /**
     *
     */
//    @TableLogic
    private Integer delFlag;


}
