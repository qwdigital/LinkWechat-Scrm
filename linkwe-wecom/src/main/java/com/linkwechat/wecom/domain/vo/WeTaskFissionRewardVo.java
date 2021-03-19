package com.linkwechat.wecom.domain.vo;

import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.wecom.domain.WeTaskFissionReward;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务裂变奖励对象 we_task_fission_reward
 *
 * @author leejoker
 * @date 2021-01-20
 */
@ApiModel
@Data
public class WeTaskFissionRewardVo extends BaseEntity {
    @ApiModelProperty("奖励对象")
    private WeTaskFissionReward weTaskFissionReward;

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
}
