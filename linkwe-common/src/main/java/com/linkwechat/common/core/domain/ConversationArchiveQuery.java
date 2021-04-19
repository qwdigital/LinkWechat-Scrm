package com.linkwechat.common.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 会话存档接口入参实体
 * @date 2020/12/29 14:23
 **/
@ApiModel
@Data
public class ConversationArchiveQuery extends BaseEntity {
    /** 发送人Id */
    @ApiModelProperty("发送人Id")
    private String fromId="";

    /** 成员名称 */
    @ApiModelProperty("成员名称")
    private String userName="";

    /** 接收人Id */
    @ApiModelProperty("接收人Id")
    private String receiveId="";

    /** 客户姓名 */
    @ApiModelProperty("客户姓名")
    private String customerName="";

    /** 群聊Id */
    @ApiModelProperty("群聊Id")
    private String roomId="";

    /** 消息类型（同企微api文档消息类型） */
    @ApiModelProperty("消息类型（同企微api文档消息类型）")
    private String msgType="";

    /** 关键词 **/
    @ApiModelProperty("关键词")
    private String keyWord="";

    /** 消息动作 */
    @ApiModelProperty("消息动作")
    private String action="";
}
