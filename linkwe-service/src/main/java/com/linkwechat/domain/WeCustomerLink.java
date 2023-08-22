package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.media.WeMessageTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 获客助手
 * @TableName we_customer_link
 */
@TableName(value ="we_customer_link")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeCustomerLink extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 链接名称
     */
    private String linkName;

    /**
     * 是否无需验证，默认为true 1:是 0:是
     */
    private Integer skipVerify;

    /**
     * 此获客链接关联的userid列表，最多可关联100个
     */
    private String weUserList;

    /**
     * 此获客链接关联的部门id列表，部门覆盖总人数最多100个
     */
    private String departmentList;

    /**
     * 标签id多个使用逗号隔开
     */
    private String tagIds;

    /**
     * 获客链接id
     */
    private String linkId;

    /**
     * 获客链接
     */
    private String linkUrl;


    /**
     * 获客链接短链
     */
    private String linkShortUrl;


    /**
     * 渠道标识
     */
    private String state;


    /**
     * 附件(传入)
     */
    @TableField(exist = false)
    private List<WeMessageTemplate> attachments;


    /**
     * 回显
     */
    @TableField(exist = false)
    private List<WeCustomerLinkAttachments> linkAttachments;

    /**
     * 删除标识 0 有效 1删除
     */
    @TableLogic
    private Integer delFlag;


    /**
     * 多个标签名使用逗号隔开
     */
    @TableField(exist = false)
    private String tagNames;


    /**
     * 多个员工名称使用逗号隔开
     */
    @TableField(exist = false)
    private String weUserNames;







}