package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/8/13 22:35
 */
@ApiModel
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("we_chat_contact_sensitive_msg")
public class WeChatContactSensitiveMsg extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1909244243128889376L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 主键id
     */
    @Excel(name = "消息id")
    @ApiModelProperty("消息id")
    private String msgId;

    @Excel(name = "通知发送状态")
    @ApiModelProperty("通知发送状态, 0 未发送 1 已发送")
    private Integer sendStatus;

    @Excel(name = "匹配词")
    @ApiModelProperty("匹配词")
    private String patternWords;

    @Excel(name = "匹配的内容")
    @ApiModelProperty("匹配的内容")
    private String content;

    @Excel(name = "发送用户id")
    @ApiModelProperty("发送用户id")
    @TableField(value = "from_id")
    private String fromId;

    @Excel(name = "发送时间")
    @ApiModelProperty("发送时间")
    private Date msgTime;
}
