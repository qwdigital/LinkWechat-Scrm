package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 群聊数据统计数据
 * (WeGroupStatistic)
 *
 * @author danmo
 * @since 2022-04-30 23:28:18
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_group_statistic")
public class WeGroupStatistic extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 数据日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "数据日期")
    @TableField("stat_time")
    private Date statTime;


    /**
     * 新增客户群数量
     */
    @ApiModelProperty(value = "新增客户群数量")
    @TableField("new_chat_cnt")
    private Integer newChatCnt;


    /**
     * 截至当天客户群总数量
     */
    @ApiModelProperty(value = "截至当天客户群总数量")
    @TableField("chat_total")
    private Integer chatTotal;


    /**
     * 截至当天有发过消息的客户群数量
     */
    @ApiModelProperty(value = "截至当天有发过消息的客户群数量")
    @TableField("chat_has_msg")
    private Integer chatHasMsg;


    /**
     * 客户群新增群人数
     */
    @ApiModelProperty(value = "客户群新增群人数")
    @TableField("new_member_cnt")
    private Integer newMemberCnt;


    /**
     * 截至当天客户群总人数
     */
    @ApiModelProperty(value = "截至当天客户群总人数")
    @TableField("member_total")
    private Integer memberTotal;


    /**
     * 截至当天有发过消息的群成员数
     */
    @ApiModelProperty(value = "截至当天有发过消息的群成员数")
    @TableField("member_has_msg")
    private Integer memberHasMsg;


    /**
     * 截至当天客户群消息总数
     */
    @ApiModelProperty(value = "截至当天客户群消息总数")
    @TableField("msg_total")
    private Integer msgTotal;


    
    
    


    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    @TableField("del_flag")
    private Integer delFlag;
}
