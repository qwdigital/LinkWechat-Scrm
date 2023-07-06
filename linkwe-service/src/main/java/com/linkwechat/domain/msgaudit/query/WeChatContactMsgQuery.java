package com.linkwechat.domain.msgaudit.query;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author danmo
 * @date 2023年07月06日 10:32
 */
@ApiModel
@Data
public class WeChatContactMsgQuery {

    /**
     * 消息id
     */
    @ApiModelProperty(value = "消息id")
    private String msgId;


    /**
     * 发送人id
     */
    @ApiModelProperty(value = "发送人id")
    private String fromId;

    /**
     * 接收人id（列表）
     */
    @ApiModelProperty(value = "接收人id（列表）")
    private String toList;

    /**
     * 群聊id
     */
    @ApiModelProperty(value = "群聊id")
    private String roomId;


    /**
     * 消息类型
     */
    @ApiModelProperty(value = "消息类型")
    private String action;

    /**
     * 消息类型(如：文本，图片)
     */
    @ApiModelProperty(value = "消息类型(如：文本，图片)")
    private String msgType;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "发送时间")
    private Date msgTime;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容")
    private String contact;

    /**
     * 是否为外部聊天 0 外部 1 内部
     */
    @ApiModelProperty(value = "是否为外部聊天 0 外部 1 内部")
    private Integer isExternal;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("开始时间")
    private Date beginTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     *  是否为全局检索 1-是
     */
    @ApiModelProperty(value = "是否为全局检索 1-是")
    private Integer fullSearch = 0;

    @ApiModelProperty("成员名称")
    private String userName;

    @ApiModelProperty("客户名称")
    private String customerName;
}
