package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.annotation.Excel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 联系客户统计数据 对象 we_user_behavior_data
 *
 * @author ruoyi
 * @date 2021-02-24
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("we_user_behavior_data")
public class WeUserBehaviorData implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * $column.columnComment
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 客户id
     */
    @Excel(name = "客户id")
    private String userId;

    /**
     * 数据日期，为当日0点的时间戳
     */
    @Excel(name = "数据日期，为当日0点的时间戳", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date statTime;

    /**
     * 发起申请数
     */
    @Excel(name = "发起申请数")
    private Integer newApplyCnt;

    /**
     * 新增客户数，成员新添加的客户数量
     */
    @Excel(name = "新增客户数，成员新添加的客户数量")
    private Integer newContactCnt;

    /**
     * 聊天总数， 成员有主动发送过消息的单聊总数
     */
    @Excel(name = "聊天总数， 成员有主动发送过消息的单聊总数")
    private Integer chatCnt;

    /**
     * 发送消息数，成员在单聊中发送的消息总数
     */
    @Excel(name = "发送消息数，成员在单聊中发送的消息总数")
    private Integer messageCnt;

    /**
     * 已回复聊天占比，浮点型，客户主动发起聊天后，成员在一个自然日内有回复过消息的聊天数/客户主动发起的聊天数比例，不包括群聊，仅在确有聊天时返回
     */
    @Excel(name = "已回复聊天占比，浮点型，客户主动发起聊天后，成员在一个自然日内有回复过消息的聊天数/客户主动发起的聊天数比例，不包括群聊，仅在确有聊天时返回")
    private Float replyPercentage;

    /**
     * 平均首次回复时长
     */
    @Excel(name = "平均首次回复时长")
    private Integer avgReplyTime;

    /**
     * 删除/拉黑成员的客户数，即将成员删除或加入黑名单的客户数
     */
    @Excel(name = "删除/拉黑成员的客户数，即将成员删除或加入黑名单的客户数")
    private Integer negativeFeedbackCnt;

    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();
}
