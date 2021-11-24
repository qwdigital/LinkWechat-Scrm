package com.linkwechat.wecom.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 社群运营相关任务执行人信息
 */
@ApiModel("社群运营任务跟进人")
@Data
public class WeCommunityTaskEmplVo implements Serializable {

    private static final long serialVersionUID = 1004068763061592787L;
    /**
     * 员工id
     */
    @ApiModelProperty("员工id")
    private String userId;

    /**
     * 员工名称
     */
    @ApiModelProperty("员工名称")
    private String name;

    /**
     * 员工头像
     */
    @ApiModelProperty("员工头像")
    private String avatar;

    /**
     * 是否已完成任务
     */
    @ApiModelProperty("是否已完成任务")
    private boolean isDone;
}
