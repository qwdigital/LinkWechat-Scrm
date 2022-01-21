package com.linkwechat.wecom.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * 首页数据统计表(WePageStatistics)
 *
 * @author danmo
 * @since 2021-11-16 16:20:32
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_page_statistics")
public class WePageStatistics extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 新增客户数
     */
    @ApiModelProperty(value = "新增客户数")
    @TableField("new_contact_cnt")
    private Integer newContactCnt;


    /**
     * 新增客群数
     */
    @ApiModelProperty(value = "新增客群数")
    @TableField("new_chat_cnt")
    private Integer newChatCnt;


    /**
     * 群新增人数
     */
    @ApiModelProperty(value = "群新增人数")
    @TableField("new_member_cnt")
    private Integer newMemberCnt;


    /**
     * 流失客户数
     */
    @ApiModelProperty(value = "流失客户数")
    @TableField("negative_feedback_cnt")
    private Integer negativeFeedbackCnt;


    /**
     * 统计时间
     */
    @ApiModelProperty(value = "统计时间")
    @TableField("refresh_time")
    private Date refreshTime;


    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    @TableField("del_flag")
    private Integer delFlag;
}
