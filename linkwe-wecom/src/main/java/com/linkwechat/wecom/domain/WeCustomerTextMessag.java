package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群发消息  文本消息表 we_customer_textMessage
 *
 * @author kewen
 * @date 2020-12-05
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_customer_textMessage")
public class WeCustomerTextMessag extends BaseEntity {

    /**
     * 主键id
     */
    private Long textMessageId;

    /**
     * 微信消息表id
     */
    private Long messageId;

    /**
     * 消息文本内容，最多4000个字节
     */
    private String content;

    /**
     * 0 未删除 1 已删除
     */
    private int delFlag;

}
