package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 智能表单统计(按照每天的维度统计相关客户数据；ip+当天定位每一条记录)
 * @TableName we_form_survey_count
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="we_form_survey_count")
public class WeFormSurveyCount extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 总访问量(每天记录对应当天对应ip的访问量)
     */
    private Long totalVisits;

    /**
     * 完成总时间
     */
     private Long totalTime;

    /**
     * 问卷id
     */
    private Long belongId;

    /**
     * 访问ip
     */
    private String visitorIp;


    /**
     * 删除标识 0 正常 1 删除
     */
    @TableLogic
    private Integer delFlag;


    @TableField(exist = false)
    private String channelsName;


    /**
     * 数据来源
     */
    private String dataSource;


}