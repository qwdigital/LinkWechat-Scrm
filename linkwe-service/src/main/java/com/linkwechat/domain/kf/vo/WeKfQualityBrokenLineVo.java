package com.linkwechat.domain.kf.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @date 2022年11月29日 11:20
 */
@ExcelIgnoreUnannotated
@ApiModel
@Data
public class WeKfQualityBrokenLineVo {

    /**
     * 日期
     */
    @ExcelProperty(value = "日期",order = 0)
    @ApiModelProperty(value = "日期")
    private String dateTime;

    /**
     * 参评总数
     */
    @ExcelProperty(value = "参评总数",order = 1)
    @ApiModelProperty(value = "参评总数")
    private Integer evaluateCnt = 0;

    /**
     * 参评率
     */
    @ExcelProperty(value = "参评率",order = 2)
    @ApiModelProperty(value = "参评率")
    private String evaluateRate = "0%";

    /**
     * 好评率
     */
    @ExcelProperty(value = "好评率",order = 3)
    @ApiModelProperty(value = "好评率")
    private String goodRate = "0%";


    /**
     * 一般率
     */
    @ExcelProperty(value = "一般率",order = 4)
    @ApiModelProperty(value = "一般率")
    private String commonRate = "0%";


    /**
     * 差评率
     */
    @ExcelProperty(value = "差评率",order = 5)
    @ApiModelProperty(value = "差评率")
    private String badRate = "0%";
}
