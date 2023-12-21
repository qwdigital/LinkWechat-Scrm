package com.linkwechat.domain.form.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author danmo
 * @date 2022年09月20日 18:24
 */
@Data
public class WeFormSurveyStatisticQuery extends BaseEntity {

    /**
     * 查询时间类型
     * customization 自定义 当为自定义类型时，开始时间和结束时间为必填
     * week 周
     * month 月
     */
    @ApiModelProperty(value = "查询时间类型")
    private String type;

    /**
     * 问卷id
     */
    @ApiModelProperty(value = "问卷id")
    //@NotNull(message = "问卷ID不能为空")
    private Long belongId;


    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "问卷答案")
    private String answer;

    @ApiModelProperty(value = "数据来源")
    private String dataSource;
}
