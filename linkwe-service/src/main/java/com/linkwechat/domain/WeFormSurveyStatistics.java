package com.linkwechat.domain;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 问卷-统计表(WeFormSurveyStatistics)
 *
 * @author danmo
 * @since 2022-09-20 18:02:57
 */
@Data
@SuppressWarnings("serial")
@TableName("we_form_survey_statistics")
@ColumnWidth(25)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeFormSurveyStatistics extends BaseEntity {

    private static final long serialVersionUID = 1L; //1


    @TableId(type = IdType.AUTO)
    @TableField("id")
    @ExcelIgnore
    private Long id;


    /**
     * 问卷id
     */
    @TableField("belong_id")
    @ExcelIgnore
    private Long belongId;


    /**
     * 总访问量
     */
    @TableField("total_visits")
    @ExcelProperty(value = "总访问量", index = 2)
    private Integer totalVisits;


    /**
     * 总访问用户量
     */
    @TableField("total_user")
    @ExcelProperty(value = "总访问用户量", index = 3)
    private Integer totalUser;


    /**
     * 有效收集量
     */
    @TableField("collection_volume")
    @ExcelProperty(value = "有效收集量", index = 4)
    private Integer collectionVolume;


    /**
     * 收集率
     */
    @TableField("collection_rate")
    @ExcelProperty(value = "收集率", index = 5)
    private String collectionRate;


    /**
     * 平均完成时间
     */
    @TableField("average_time")
    @ExcelProperty(value = "平均完成时间", index = 6)
    private String averageTime;


    /**
     * 较昨日总访问量
     */
    @TableField("yes_total_visits")
    @ExcelIgnore
    private Integer yesTotalVisits;


    /**
     * 较昨日总访问用户量
     */
    @TableField("yes_total_user")
    @ExcelIgnore
    private Integer yesTotalUser;


    /**
     * 较昨日有效收集量
     */
    @TableField("yes_collection_volume")
    @ExcelIgnore
    private Integer yesCollectionVolume;


    /**
     * 来源渠道
     */
    @TableField("data_source")
    @ExcelIgnore
    private String dataSource;


    /**
     * 删除标识 0 正常 1 删除
     */
    @TableField("del_flag")
    @ExcelIgnore
    private Integer delFlag;

    @TableField(exist = false)
    @ExcelProperty(value = "日期", index = 1)
    private String date;
}
