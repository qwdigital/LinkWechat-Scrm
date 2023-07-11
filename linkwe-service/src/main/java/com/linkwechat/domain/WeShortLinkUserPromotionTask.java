package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 员工短链推广任务
 * </p>
 *
 * @author WangYX
 * @since 2023-03-09
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("we_short_link_user_promotion_task")
public class WeShortLinkUserPromotionTask extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键Id")
    private Long id;

    /**
     * 员工Id
     */
    @ApiModelProperty(value = "员工Id")
    private String userId;

    /**
     * 任务所属类型：0群发客户 1群发客群 2朋友圈
     */
    @ApiModelProperty(value = "任务所属类型：0群发客户 1群发客群 2朋友圈")
    private Integer templateType;

    /**
     * 短链推广模板Id
     */
    @ApiModelProperty(value = "短链推广模板Id")
    private Long templateId;

    /**
     * 发送状态 0未发送 2已发送
     */
    @ApiModelProperty(value = "发送状态 0未发送 2已发送 3已取消")
    private Integer sendStatus;

    /**
     * 企业微信端返回的消息id
     */
    @ApiModelProperty(value = "企业微信端返回的消息id")
    private String msgId;

    /**
     * 预计发送客户数量
     */
    @ApiModelProperty(value = "预计发送客户数量")
    private Integer allClientNum;

    /**
     * 实际发送客户数量
     */
    @ApiModelProperty(value = "实际发送客户数量")
    private Integer realClientNum;

    /**
     * 预计发送客群数量
     */
    @ApiModelProperty(value = "预计发送客群数量")
    private Integer allGroupNum;

    /**
     * 实际发送客群数量
     */
    @ApiModelProperty(value = "实际发送客群数量")
    private Integer realGroupNum;

    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    private Integer delFlag;

    /**
     * 朋友圈Id
     */
    @ApiModelProperty(value = "朋友圈Id")
    private String momentId;

}
