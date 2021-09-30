package com.linkwechat.wecom.domain.dto;

import com.linkwechat.wecom.domain.dto.message.ImageMessageDto;
import com.linkwechat.wecom.domain.dto.message.LinkMessageDto;
import com.linkwechat.wecom.domain.dto.message.MiniprogramMessageDto;
import com.linkwechat.wecom.domain.dto.message.TextMessageDto;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@SuppressWarnings("all")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeMessagePushDto {

    /**
     * 指定接收消息的成员，成员ID列表（多个接收者用‘|’分隔，最多支持1000个）。
     * 特殊情况：指定为”@all”，则向该企业应用的全部成员发送
     */
    private String touser;

    /**
     * 指定接收消息的部门，部门ID列表，多个接收者用‘|’分隔，最多支持100个。
     * 当touser为”@all”时忽略本参数
     */
    private String toparty;

    /**
     * 指定接收消息的标签，标签ID列表，多个接收者用‘|’分隔，最多支持100个。
     * 当touser为”@all”时忽略本参数
     */
    private String totag;

    /**
     * 消息类型，此时固定为：text
     */
    private String msgtype;

    /**
     * 企业应用的id，整型。企业内部开发，可在应用的设置页面查看；第三方服务商，可通过接口
     * <a href="https://work.weixin.qq.com/api/doc/10975#%E8%8E%B7%E5%8F%96%E4%BC%81%E4%B8%9A%E6%8E%88%E6%9D%83%E4%BF%A1%E6%81%AF">获取企业授权信息</a> 获取该参数值
     */
    private Integer agentid;

    /**
     * 表示是否是保密消息，0表示否，1表示是，默认0
     */
    private Integer safe;

    /**
     * 表示是否开启id转译，0表示否，1表示是，默认0
     */
    private Integer enable_id_trans;

    /**
     * 表示是否开启重复消息检查，0表示否，1表示是，默认0
     */
    private Integer enable_duplicate_check;

    /**
     * 表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时
     */
    private Long duplicate_check_interval;

    /**
     *  根据msgtype的值不同添加一个属性和对应的属性值
     */

    /**
     *文本消息
     */
    private TextMessageDto text;

    /**
     * 图片消息
     */
    private ImageMessageDto image;

    /**
     * 链接消息
     */
    private LinkMessageDto link;

    /**
     * 小程序消息
     */
    private MiniprogramMessageDto miniprogram;

}
