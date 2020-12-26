package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 群发消息  微信消息表 we_customer_message
 *
 * @author kewen
 * @date 2020-12-05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_customer_message")
public class WeCustomerMessage extends BaseEntity {

    /**
     * 主键id
     */
    private Long messageId;

    /**
     * 原始数据表id
     */
    private Long originalId;

    /**
     * 群发任务的类型，默认为single，表示发送给客户，group表示发送给客户群
     */
    private String chatType;

    /**
     * 客户的外部联系人id列表JSON
     */
    private String externalUserid;

    /**
     * 发送企业群发消息的成员userid，当类型为发送给客户群时必填(和企微客户沟通后确认是群主id)
     */
    private String sender;

    /**
     * 消息发送状态 0 未发送  1 已发送
     */
    private String checkStatus;

    /**
     * 企业群发消息的id，可用于<a href="https://work.weixin.qq.com/api/doc/90000/90135/92136">获取群发消息发送结果</a>
     */
    private String msgid;

    /**
     * 发送情况
     * ps:显示样式
     *
     * 1）定时任务 发送时间:2020-12-25 10:00:00
     * 2) 预计发送3190人，已成功发送0人
     * 3）预计发送2个群，已成功发送0个群
     *
     */
    private String sendInfo;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 0 未删除 1 已删除
     */
    private int delFlag;

}
