package com.linkwechat.wecom.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 消息发送的对象 we_message_push
 *
 * @author KeWen
 * @date 2020-10-28
 */
@Data
public class WeMessagePush extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long messagePushId;

    /**
     * 群发类型 0 发给客户 1 发给客户群
     */

    private Integer pushType;

    /**
     * 消息类型 0 文本消息  1 图片消息 2 语音消息  3 视频消息    4 文件消息 5 文本卡片消息 6 图文消息
     * 7 图文消息（mpnews） 8 markdown消息 9 小程序通知消息 10 任务卡片消息
     */
    private String messageType;

    /**
     * 消息体
     */
    private String messageJson;

    /**
     * 消息范围 0 全部客户  1 指定客户
     */
    private String pushRange;

    /**
     * 0 未删除 1 已删除
     */
    private Integer delFlag;

    /**
     * 无效用户
     */
    private String invaliduser;

    /**
     * 无效单位
     */
    private String invalidparty;

    /**
     * 无效标签
     */
    private String invalidtag;

    /**
     * 指定接收消息的成员
     */
    private String toUser;

    /**
     * 指定接收消息的部门
     */
    private String toParty;

    /**
     * 指定接收消息的标签
     */
    private String toTag;

    /**
     * 群聊id
     */
    private String chatId;

    /**
     * 企业应用的id，整型。企业内部开发，可在应用的设置页面查看；第三方服务商，可通过接口
     * <a href="https://work.weixin.qq.com/api/doc/10975#%E8%8E%B7%E5%8F%96%E4%BC%81%E4%B8%9A%E6%8E%88%E6%9D%83%E4%BF%A1%E6%81%AF">获取企业授权信息</a> 获取该参数值
     */
    private Integer agentid;


}
