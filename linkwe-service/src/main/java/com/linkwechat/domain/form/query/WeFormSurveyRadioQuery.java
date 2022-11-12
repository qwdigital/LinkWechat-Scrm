package com.linkwechat.domain.form.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author danmo
 * @date 2022年09月20日 18:24
 */
@Data
public class WeFormSurveyRadioQuery {

    @ApiModelProperty(value = "表单Id")
    private String formId;

    /** 开始时间 */
    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /** 结束时间 */
    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @ApiModelProperty(value = "数据来源")
    private String dataSource;

    /**
     * 题号
     */
    @ApiModelProperty(value = "题号")
    private String questionNumber;

    @ApiModelProperty(value = "控件名称")
    private String label;

    @ApiModelProperty(value = "选择内容")
    private String defaultValue;

    @ApiModelProperty(value = "上级地区id")
    private Integer parentCode = 0;

    @ApiModelProperty(value = "当前地区id",required = false)
    private Integer code;
}
