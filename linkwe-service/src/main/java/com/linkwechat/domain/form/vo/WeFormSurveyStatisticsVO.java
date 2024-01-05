package com.linkwechat.domain.form.vo;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.linkwechat.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 问卷-统计表(WeFormSurveyStatistics)
 */
@ExcelIgnoreUnannotated
@Data
public class WeFormSurveyStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
    @ExcelProperty("日期")
    private Date createTime;


    /**
     * 总访问量
     */
    @ExcelProperty("总访问量")
    private Integer totalVisits;

    /**
     * 总访问用户量
     */
    @ExcelProperty("总访问用户量")
    private Integer totalUser;

    /**
     * 有效收集量
     */
    @ExcelProperty("有效收集量")
    private Integer collectionVolume;


    /**
     * 收集率
     */
    @ExcelProperty("收集率")
    private String collectionRate;


    /**
     * 平均完成时间
     */
    @ExcelProperty("平均完成时间")
    private Integer averageTime;

}
