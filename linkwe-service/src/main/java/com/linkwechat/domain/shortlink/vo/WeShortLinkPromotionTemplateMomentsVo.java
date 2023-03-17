package com.linkwechat.domain.shortlink.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 短链推广模板-朋友圈
 *
 * @author WangYX
 * @since 2023-03-10
 */
@ApiModel
@Data
public class WeShortLinkPromotionTemplateMomentsVo {

    private static final long serialVersionUID = 1L;

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
     * 群发朋友圈分类 0全部客户 1部分客户
     */
    @ApiModelProperty(value = "群发朋友圈分类 0全部客户 1部分客户")
    private Integer scopeType;

    /**
     * 群发朋友圈分类 0全部客户 1部分客户
     */
    @ApiModelProperty(value = "群发朋友圈分类 0全部客户 1部分客户")
    private Integer type;

    /**
     * 添加员工Id
     */
    @ApiModelProperty(value = "添加员工Id")
    private String userIds;

    /**
     * 添加员工
     */
    @ApiModelProperty(value = "添加员工")
    private Map<String, String> users;

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
     * 客户标签
     */
    @ApiModelProperty(value = "客户标签")
    private Map<String, String> labels;

    /**
     * 跟进状态：1:待跟进;2:跟进中;3:已成交;4:无意向;5:已流失
     */
    @ApiModelProperty(value = "跟进状态：1:待跟进;2:跟进中;3:已成交;4:无意向;5:已流失")
    private Integer trackState;

    /**
     * 动态文本
     */
    @ApiModelProperty(value = "动态文本")
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


}
