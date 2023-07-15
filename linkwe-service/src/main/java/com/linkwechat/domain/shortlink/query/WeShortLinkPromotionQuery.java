package com.linkwechat.domain.shortlink.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * <p>
 * 短链推广
 * </p>
 *
 * @author WangYX
 * @since 2023-03-07
 */
@ApiModel
@Data
public class WeShortLinkPromotionQuery {

    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称")
    private String taskName;

    /**
     * 短链名称
     */
    @ApiModelProperty(value = "短链名称")
    private String shortLinkName;

    /**
     * 推广短链Id
     */
    @ApiModelProperty(value = "推广短链Id")
    private Long shortLinkId;

    /**
     * 推广方式：0群发客户，1群发客户群，2群发朋友圈，4应用消息
     */
    @ApiModelProperty(value = "推广方式：0群发客户，1群发客户群，2群发朋友圈，4应用消息")
    private Integer type;

    /**
     * 推广样式：0短链二维码 1短链海报
     */
    @ApiModelProperty(value = "推广样式：0短链二维码 1短链海报")
    private Integer style;

    /**
     * 任务状态: 0带推广 1推广中 2已结束
     */
    @ApiModelProperty(value = "任务状态: 0待推广 1推广中 2已结束")
    private Integer taskStatus;
}
