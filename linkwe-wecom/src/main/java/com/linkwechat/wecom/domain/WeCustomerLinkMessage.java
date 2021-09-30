package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群发消息  链接消息表 we_customer_linkMessage
 *
 * @author kewen
 * @date 2020-12-05
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_customer_linkMessage")
public class WeCustomerLinkMessage extends BaseEntity {

    /**
     * 主键id
     */
    private Long linkMessageId;

    /**
     * 微信消息表id
     */
    private Long messageId;

    /**
     * 图文消息标题
     */
    private String title;

    /**
     * 图文消息封面的url
     */
    private String picurl;

    /**
     * 图文消息的描述，最多512个字节
     */
    private String desc;

    /**
     * 图文消息的链接
     */
    private String url;

    /**
     * 0 未删除 1 已删除
     */
    private int delFlag;

}
