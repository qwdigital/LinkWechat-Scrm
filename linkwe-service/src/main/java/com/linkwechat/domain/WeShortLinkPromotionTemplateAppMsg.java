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
 * 短链推广模板-应用消息
 * </p>
 *
 * @author WangYX
 * @since 2023-03-14
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("we_short_link_promotion_template_app_msg")
public class WeShortLinkPromotionTemplateAppMsg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 短链推广Id
     */
    @ApiModelProperty(value = "短链推广Id")
    private Long promotionId;

    /**
     * 员工Id，多个逗号分隔
     */
    @ApiModelProperty(value = "员工Id，多个逗号分隔")
    private String userIds;

    /**
     * 部门Id，多个逗号分隔
     */
    @ApiModelProperty(value = "部门Id，多个逗号分隔")
    private String deptIds;

    /**
     * 岗位Id，多个逗号分隔
     */
    @ApiModelProperty(value = "岗位Id，多个逗号分隔")
    private String postIds;

    /**
     * 发送类型：0立即发送 1定时发送
     */
    @ApiModelProperty(value = "发送类型：0立即发送 1定时发送")
    private Integer sendType;

    /**
     * 定时发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "定时发送时间")
    private LocalDateTime taskSendTime;

    /**
     * 任务结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "任务结束时间")
    private LocalDateTime taskEndTime;

    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    private Integer delFlag;


}
