package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群发消息  小程序消息表 we_customer_miniprogramMessage
 *
 * @author kewen
 * @date 2020-12-05
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_customer_miniprogramMessage")
public class WeCustomerMiniprogramMessage extends BaseEntity {

    /**
     * 主键id
     */
    private Long miniprogramMessageId;

    /**
     * 微信消息表id
     */
    private Long messageId;

    /**
     * 小程序消息标题，最多64个字节
     */
    private String title;

    /**
     * 小程序消息封面的mediaid，封面图建议尺寸为520*416
     */
    private String picMediaId;

    /**
     * 小程序appid，必须是关联到企业的小程序应用
     */
    private String appid;

    /**
     * 小程序page路径
     */
    private String page;

    /**
     * 0 未删除 1 已删除
     */
    private int delFlag;

}
