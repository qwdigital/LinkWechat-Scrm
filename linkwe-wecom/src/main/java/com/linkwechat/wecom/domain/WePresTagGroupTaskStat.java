package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.linkwechat.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.NoArgsConstructor;

/**
 * 老客户标签建群客户统计(用于个人群发类型的统计)
 */
@ApiModel("老客标签建群发送统计对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("we_pres_tag_group_stat")
public class WePresTagGroupTaskStat extends BaseEntity {

    private static final long serialVersionUID = 504772877084507165L;
    /**
     * 老客户标签建群任务id
     */
    @ApiModelProperty("老客户标签建群任务id")
    private Long taskId;

    /**
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    @TableField(exist = false)
    private String customerName;

    /**
     * 跟进者id
     */
    @ApiModelProperty("跟进者")
    private String userId;

    /**
     * 客户external_id
     */
    @ApiModelProperty("老客户标签建群任务id")
    @TableField(value = "external_userid")
    private String externalUserId;

    /**
     * 是否已送达
     */
    @ApiModelProperty("老客户标签建群任务id")
    private Integer sent = 0;

    /**
     * 是否已经在群
     */
    @ApiModelProperty("老客户标签建群任务id")
    @TableField(exist = false)
    private Integer inGroup = 0;

    @ApiModelProperty(value = "有效标识", hidden = true)
    @JsonIgnore
    private Integer delFlag = 0;
}
