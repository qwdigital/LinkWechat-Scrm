package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 问卷-统计表(WeFormSurveyStatistics)
 *
 * @author danmo
 * @since 2022-09-20 18:02:57
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_form_survey_statistics")
public class WeFormSurveyStatistics extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1


    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 问卷id
     */
    @ApiModelProperty(value = "问卷id")
    @TableField("belong_id")
    private Long belongId;


    /**
     * 总访问量
     */
    @ApiModelProperty(value = "总访问量")
    @TableField("total_visits")
    private Integer totalVisits;


    /**
     * 总访问用户量
     */
    @ApiModelProperty(value = "总访问用户量")
    @TableField("total_user")
    private Integer totalUser;


    /**
     * 有效收集量
     */
    @ApiModelProperty(value = "有效收集量")
    @TableField("collection_volume")
    private Integer collectionVolume;


    /**
     * 收集率
     */
    @ApiModelProperty(value = "收集率")
    @TableField("collection_rate")
    private String collectionRate;


    /**
     * 平均完成时间
     */
    @ApiModelProperty(value = "平均完成时间")
    @TableField("average_time")
    private Integer averageTime;


    /**
     * 较昨日总访问量
     */
    @ApiModelProperty(value = "较昨日总访问量")
    @TableField("yes_total_visits")
    private Integer yesTotalVisits;


    /**
     * 较昨日总访问用户量
     */
    @ApiModelProperty(value = "较昨日总访问用户量")
    @TableField("yes_total_user")
    private Integer yesTotalUser;


    /**
     * 较昨日有效收集量
     */
    @ApiModelProperty(value = "较昨日有效收集量")
    @TableField("yes_collection_volume")
    private Integer yesCollectionVolume;


    /**
     * 来源渠道
     */
    @ApiModelProperty(value = "来源渠道")
    @TableField("data_source")
    private String dataSource;


    /**
     * 删除标识 0 正常 1 删除
     */
    @ApiModelProperty(value = "删除标识 0 正常 1 删除")
    @TableField("del_flag")
    private Integer delFlag;
}
