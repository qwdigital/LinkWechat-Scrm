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
 * 客服客户统计表(WeKfCustomerStat)
 *
 * @author danmo
 * @since 2022-11-28 16:48:24
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_kf_customer_stat")
public class WeKfCustomerStat extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 日期
     */
    @ApiModelProperty(value = "日期")
    @TableField("date_time")
    private String dateTime;


    /**
     * 会话总数
     */
    @ApiModelProperty(value = "会话总数")
    @TableField("session_cnt")
    private Integer sessionCnt;


    /**
     * 参评总数
     */
    @ApiModelProperty(value = "参评总数")
    @TableField("evaluate_cnt")
    private Integer evaluateCnt;


    /**
     * 好评数
     */
    @ApiModelProperty(value = "好评数")
    @TableField("good_cnt")
    private Integer goodCnt;


    /**
     * 一般数
     */
    @ApiModelProperty(value = "一般数")
    @TableField("common_cnt")
    private Integer commonCnt;


    /**
     * 差评数
     */
    @ApiModelProperty(value = "差评数")
    @TableField("bad_cnt")
    private Integer badCnt;


    /**
     * 是否删除:0有效,1删除
     */
    @ApiModelProperty(value = "是否删除:0有效,1删除")
    @TableField("del_flag")
    private Integer delFlag;


}
