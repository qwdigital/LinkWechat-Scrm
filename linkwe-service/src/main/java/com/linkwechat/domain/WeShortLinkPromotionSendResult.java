package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 短链推广发送结果
 * </p>
 *
 * @author WangYX
 * @since 2023-03-08
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("we_short_link_promotion_send_result")
public class WeShortLinkPromotionSendResult extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 附件所属类型：0群发客户 1群发客群 2朋友圈
     */
    @ApiModelProperty(value = "附件所属类型：0群发客户 1群发客群 2朋友圈")
    private Integer templateType;

    /**
     * 短链推广模板Id
     */
    @ApiModelProperty(value = "短链推广模板Id")
    private Long templateId;

    /**
     * 员工短链推广任务Id
     */
    @ApiModelProperty(value = "员工短链推广任务Id")
    private Long userPromotionId;

    /**
     * 员工Id
     */
    @ApiModelProperty(value = "员工Id")
    private String userId;

    /**
     * 外部联系人Id
     */
    @ApiModelProperty(value = "外部联系人Id")
    private String externalUserid;

    /**
     * 外部联系群Id
     */
    @ApiModelProperty(value = "外部联系群Id")
    private String chatId;

    /**
     * 发送状态：0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败
     */
    @ApiModelProperty(value = "发送状态：0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败")
    private Integer status;

    /**
     * 发送时间，发送状态为1时返回
     */
    @ApiModelProperty(value = "发送时间，发送状态为1时返回")
    private LocalDateTime sendTime;

    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    private Integer delFlag;

}
