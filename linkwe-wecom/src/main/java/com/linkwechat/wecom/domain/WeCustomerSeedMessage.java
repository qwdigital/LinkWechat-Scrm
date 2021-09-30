package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群发消息  群发消息  子消息表(包括 文本消息、图片消息、链接消息、小程序消息)  we_customer_seedMessage
 *
 * @author kewen
 * @date 2020-12-28
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_customer_seedMessage")
public class WeCustomerSeedMessage extends BaseEntity {

    /**
     * 主键id
     */
    private Long seedMessageId;

    /**
     * 微信消息表id
     */
    private Long messageId;

    /**
     * 消息文本内容，最多4000个字节
     */
    private String content;

    /**
     * 图片消息：图片的media_id，可以通过 <a href="https://work.weixin.qq.com/api/doc/90000/90135/90253">素材管理接口</a>获得'
     */
    private String mediaId;

    /**
     * 图片消息：图片的链接，仅可使用<a href="https://work.weixin.qq.com/api/doc/90000/90135/90256">上传图片接口</a>得到的链接'
     */
    private String picUrl;

    /**
     * 链接消息：图文消息标题
     */
    private String linkTitle;

    /**
     * 链接消息：图文消息封面的url
     */
    private String linkPicurl;

    /**
     * 链接消息：图文消息的描述，最多512个字节
     */
    private String linDesc;

    /**
     * 链接消息：图文消息的链接
     */
    private String linkUrl;

    /**
     * 小程序消息标题，最多64个字节
     */
    private String miniprogramTitle;

    /**
     * 小程序消息封面的mediaid，封面图建议尺寸为520*416
     */
    private String miniprogramMediaId;

    /**
     * 小程序appid，必须是关联到企业的小程序应用
     */
    private String appid;

    /**
     * 小程序page路径
     */
    private String page;

}
