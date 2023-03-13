package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 短链推广-客户
 * </p>
 *
 * @author WangYX
 * @since 2023-03-08
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("we_short_link_promotion_template_client")
public class WeShortLinkPromotionTemplateClient extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 短链推广Id
     */
    @ApiModelProperty(value = "短链推广Id")
    private Long promotionId;

    /**
     * 群发客户分类：0全部客户 1部分客户
     */
    @ApiModelProperty(value = "群发客户分类：0全部客户 1部分客户")
    private Integer type;

    /**
     * 添加员工Id
     */
    @ApiModelProperty(value = "添加员工Id")
    private String userIds;

    /**
     * 性别 0-未知 1-男性 2-女性
     */
    @ApiModelProperty(value = "性别 0-未知 1-男性 2-女性")
    private Integer sex;

    /**
     * 添加开始时间
     */
    @ApiModelProperty(value = "添加开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime addBeginTime;

    /**
     * 添加结束时间
     */
    @ApiModelProperty(value = "添加结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime addEndTime;

    /**
     * 客户标签Id
     */
    @ApiModelProperty(value = "客户标签Id")
    private String labelIds;

    /**
     * 跟进状态：1:待跟进;2:跟进中;3:已成交;4:无意向;5:已流失
     */
    @ApiModelProperty(value = "跟进状态：1:待跟进;2:跟进中;3:已成交;4:无意向;5:已流失")
    private Integer trackState;

    /**
     * 推广语素材Id
     */
    @ApiModelProperty(value = "推广语素材Id")
    private Long materialId;

    /**
     * 推广语
     */
    @ApiModelProperty(value = "推广语")
    private String content;

    /**
     * 发送类型：0立即发送 1定时发送
     */
    @ApiModelProperty(value = "发送类型：0立即发送 1定时发送")
    private Integer sendType;

    /**
     * 定时发送时间
     */
    @ApiModelProperty(value = "定时发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime taskSendTime;

    /**
     * 任务结束时间
     */
    @ApiModelProperty(value = "任务结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime taskEndTime;

    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    private Integer delFlag;


}
