package com.linkwechat.domain.shortlink.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.media.WeMessageTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 短链推广模板-客群
 * </p>
 *
 * @author WangYX
 * @since 2023-03-09
 */
@ApiModel
@Data
public class WeShortLinkPromotionTemplateGroupVo {

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    private Long id;

    /**
     * 短链推广Id
     */
    @ApiModelProperty(value = "短链推广Id")
    private Long promotionId;

    /**
     * 客群分类 0全部群 1部分群
     */
    @ApiModelProperty(value = "客群分类 0全部群 1部分群")
    private Integer type;

    /**
     * 群主Id
     */
    @ApiModelProperty(value = "群主Id")
    private String userIds;

    /**
     * 群主Id
     */
    @ApiModelProperty(value = "群主")
    private Map<String, String> users;

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
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime taskEndTime;

}
