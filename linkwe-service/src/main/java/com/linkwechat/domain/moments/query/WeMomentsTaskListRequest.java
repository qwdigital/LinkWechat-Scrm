package com.linkwechat.domain.moments.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 朋友圈列表查询
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/06/06 18:04
 */
@Data
@ApiModel("朋友圈列表查询")
public class WeMomentsTaskListRequest {

    /**
     * 朋友圈任务名称
     */
    @ApiModelProperty(value = "朋友圈任务名称")
    private String name;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 发送方式: 0企微群发,1个人发送,2成员群发
     */
    @ApiModelProperty(value = "发送方式")
    private Integer sendType;

    /**
     * 任务状态：1未开始，2进行中，3已结束
     */
    @ApiModelProperty(value = "任务状态")
    private Integer status;

    /**
     * 执行开始时间
     */
    @ApiModelProperty(value = "执行开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime executeStartTime;

    /**
     * 执行结束时间
     */
    @ApiModelProperty(value = "执行结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime executeEndTime;
}
