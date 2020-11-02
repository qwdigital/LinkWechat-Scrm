package com.linkwechat.wecom.domain.dto;

import lombok.Data;

@SuppressWarnings("all")
@Data
public class WeMessagePushGroupDto {

    /**
     * 群聊id
     */
    private String chatid;

    /**
     * 消息类型
     */
    private String msgtype;

    /**
     * 表示是否是保密消息，0表示否，1表示是，默认0
     */
    private Integer safe;

    /**
     *  根据msgtype的值不同添加一个属性和对应的属性值
     */

}
