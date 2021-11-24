package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 老客户标签建群任务使用人对象
 */
@ApiModel("老客户标签建群跟进者")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("we_pres_tag_group_scope")
public class WePresTagGroupTaskScope extends BaseEntity {

    private static final long serialVersionUID = 86648978618184060L;
    /**
     * 老客户标签建群任务id
     */
    @ApiModelProperty("老客户标签建群任务id")
    private Long taskId;

    /**
     * 任务目标员工id
     */
    @ApiModelProperty("任务目标员工id")
    private String weUserId;


    @ApiModelProperty(value = "有效标识", hidden = true)
    @JsonIgnore
    private Integer delFlag = 0;
}
