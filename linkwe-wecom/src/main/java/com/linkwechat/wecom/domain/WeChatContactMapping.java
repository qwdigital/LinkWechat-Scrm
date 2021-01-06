package com.linkwechat.wecom.domain;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.linkwechat.common.annotation.Excel;
import lombok.Data;


/**
 * 聊天关系映射对象 we_chat_contact_mapping
 *
 * @author ruoyi
 * @date 2020-12-27
 */
@Data
public class WeChatContactMapping {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 发送人id
     */
    @Excel(name = "发送人id")
    private String fromId;

    /**
     * 接收人id
     */
    @Excel(name = "接收人id")
    private String receiveId;

    /**
     * 群聊id
     */
    @Excel(name = "群聊id")
    private String roomId;

    /**
     * 是否为客户 0-成员 1-客户 2-机器人
     */
    @Excel(name = "是否为客户 0-成员 1-客户 2-机器人")
    private Integer isCustom;

    /**
     * 是否为客户 0-内部 1-外部 2-群聊
     */
    @TableField(exist = false)
    private Integer searchType;

    /**
     * 内部接收人信息
     */
    @TableField(exist = false)
    private WeUser fromWeUser;

    /**
     * 外部接收人信息
     */
    @TableField(exist = false)
    private WeCustomer fromWeCustomer;

    /**
     * 内部接收人信息
     */
    @TableField(exist = false)
    private WeUser receiveWeUser;

    /**
     * 外部接收人信息
     */
    @TableField(exist = false)
    private WeCustomer receiveWeCustomer;

    /**
     * 群信息
     */
    @TableField(exist = false)
    private WeGroup roomInfo;

    /**
     * 最后一条聊天数据
     */
    @TableField(exist = false)
    private JSONObject finalChatContext;
}
