package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 短链推广每日统计
 * </p>
 *
 * @author WangYX
 * @since 2023-03-07
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("we_short_link_promotion_day_stat")
public class WeShortLinkPromotionDayStat extends BaseEntity {

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
     * 统计日期
     */
    @ApiModelProperty(value = "统计日期")
    private String statDay;

    /**
     * 访问总数
     */
    @ApiModelProperty(value = "访问总数")
    private Integer pvNum;

    /**
     * 访问总人数
     */
    @ApiModelProperty(value = "访问总人数")
    private Integer uvNum;

    /**
     * 打开小程序数量
     */
    @ApiModelProperty(value = "打开小程序数量")
    private Integer openNum;

    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    private Integer delFlag;

}
