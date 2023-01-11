package com.linkwechat.domain.strategic.crowd.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author danmo
 * @description 策略人群入参
 * @date 2021/11/7 13:49
 **/
@ApiModel
@Data
public class WeStrategicCrowdQuery{

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("开始时间(yyyy-MM-dd)")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    @ApiModelProperty("结束时间(yyyy-MM-dd)")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty(value = "状态",hidden = true)
    private Integer status;

    private Integer pageNum;

    private Integer pageSize;
}
