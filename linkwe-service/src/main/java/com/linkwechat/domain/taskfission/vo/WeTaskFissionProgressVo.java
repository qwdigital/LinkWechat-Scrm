package com.linkwechat.domain.taskfission.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 任务裂变奖励对象 we_task_fission_reward
 *
 * @author leejoker
 * @date 2021-01-20
 */
@ApiModel
@Data
public class WeTaskFissionProgressVo {

    @ApiModelProperty(value = "裂变客户数量")
    private Integer fissNum;
    /**
     * 兑奖链接
     */
    @ApiModelProperty(value = "兑奖链接")
    private String rewardUrl;

    /**
     * 兑奖链接图片
     */
    @ApiModelProperty(value = "兑奖链接图片")
    private String rewardImageUrl;

    /**
     * 兑奖规则
     */
    @ApiModelProperty(value = "兑奖规则")
    private String rewardRule;

    @ApiModelProperty(value = "完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date completeTime;

    @ApiModelProperty(value = "客户任务ID")
    private Long recordId;

    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "海报")
    private String poster;

    /**
     * 添加客户列表
     */
    @ApiModelProperty(value = "邀请客户列表")
    private List<WeTaskFissionCustomerVo> customerList;
}
