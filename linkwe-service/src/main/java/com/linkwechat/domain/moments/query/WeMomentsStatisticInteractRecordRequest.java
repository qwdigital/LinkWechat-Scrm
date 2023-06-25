package com.linkwechat.domain.moments.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 朋友圈统计-互动数据列表请求参数
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/14 18:12
 */
@Data
@ApiModel("朋友圈统计-互动数据列表请求参数")
public class WeMomentsStatisticInteractRecordRequest {

    /**
     * 朋友圈任务Id
     */
    @NotNull(message = "朋友圈任务Id必填")
    @ApiModelProperty(value = "朋友圈任务Id")
    private Long weMomentsTaskId;

    /**
     * 互动开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "互动开始时间")
    private LocalDateTime beginTime;

    /**
     * 互动结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "互动结束时间")
    private LocalDateTime endTime;

    /**
     * 企微员工Id集合
     */
    @ApiModelProperty(value = "企微员工Id集合")
    private String weUserIds;

    /**
     * 互动类型:0:评论；1:点赞
     */
    @ApiModelProperty(value = "互动类型:0:评论；1:点赞")
    private Integer interactType;

}
