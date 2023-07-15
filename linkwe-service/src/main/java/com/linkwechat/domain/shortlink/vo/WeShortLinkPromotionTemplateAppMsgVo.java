package com.linkwechat.domain.shortlink.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.sop.vo.WeSopExecuteUserConditVo;
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
public class WeShortLinkPromotionTemplateAppMsgVo {

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
     * 执行员工
     */
    @ApiModelProperty(value = "执行员工")
    public WeSopExecuteUserConditVo.ExecuteUserCondit executeUserCondit;

    /**
     * 执行部门与岗位
     */
    @ApiModelProperty(value = "执行部门与岗位")
    public WeSopExecuteUserConditVo.ExecuteDeptCondit executeDeptCondit;

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

}
