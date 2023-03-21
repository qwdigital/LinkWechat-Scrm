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
 * 短链推广
 * </p>
 *
 * @author WangYX
 * @since 2023-03-07
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("we_short_link_promotion")
public class WeShortLinkPromotion extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称")
    private String taskName;

    /**
     * 推广短链Id
     */
    @ApiModelProperty(value = "推广短链Id")
    private Long shortLinkId;

    /**
     * 推广方式：0群发客户，1群发客户群，2群发朋友圈，4应用消息
     */
    @ApiModelProperty(value = "推广方式：0群发客户，1群发客户群，2群发朋友圈，3应用消息")
    private Integer type;

    /**
     * 推广样式：0短链二维码 1短链海报
     */
    @ApiModelProperty(value = "推广样式：0短链二维码 1短链海报")
    private Integer style;

    /**
     * 海报素材Id
     */
    @ApiModelProperty(value = "海报素材Id")
    private Long materialId;

    /**
     * url地址（二维码或海报的地址）
     */
    @ApiModelProperty(value = "url地址（二维码或海报的地址）")
    private String url;

    /**
     * 任务状态: 0待推广 1推广中 2已结束
     */
    @ApiModelProperty(value = "任务状态: 0待推广 1推广中 2已结束")
    private Integer taskStatus;

    /**
     * 任务开始时间
     */
    @ApiModelProperty(value = "任务开始时间")
    private LocalDateTime taskStartTime;

    /**
     * 任务结束时间
     */
    @ApiModelProperty(value = "任务结束时间")
    private LocalDateTime taskEndTime;

    /**
     * 删除标识 0 正常 1 删除
     */
    @ApiModelProperty(value = "删除标识 0 正常 1 删除")
    private Integer delFlag;

}
