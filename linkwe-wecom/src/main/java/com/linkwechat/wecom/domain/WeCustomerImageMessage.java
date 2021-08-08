package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群发消息  图片消息表 we_customer_imageMessage
 *
 * @author kewen
 * @date 2020-12-05
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_customer_imageMessage")
public class WeCustomerImageMessage extends BaseEntity {

    /**
     * 主键id
     */
    private Long imageMessageId;

    /**
     * 微信消息表id
     */
    private Long messageId;

    /**
     * 图片的media_id，可以通过 <a href="https://work.weixin.qq.com/api/doc/90000/90135/90253">素材管理接口</a>获得
     */
    private String mediaId;

    /**
     * 图片的链接，仅可使用<a href="https://work.weixin.qq.com/api/doc/90000/90135/90256">上传图片接口</a>得到的链接
     */
    private String picUrl;

    /**
     * 0 未删除 1 已删除
     */
    private int delFlag;

}
