package com.linkwechat.wecom.domain;

import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 任务裂变奖励对象 we_task_fission_reward
 *
 * @author leejoker
 * @date 2021-01-20
 */
@ApiModel
@Data
public class WeTaskFissionReward extends BaseEntity {
    private static final long serialVersionUID = -8385656412785458012L;
    /**
     * 活动奖励主键
     */
    private Long id;

    /**
     * 任务裂变id
     */
    @Excel(name = "任务裂变id")
    @ApiModelProperty("任务裂变id")
    private Long taskFissionId;

    /**
     * 兑奖码
     */
    @Excel(name = "兑奖码")
    @ApiModelProperty("兑奖码")
    private String rewardCode;

    /**
     * 兑奖码状态，0 未使用 1 已使用
     */
    @Excel(name = "兑奖码状态，0 未使用 1 已使用")
    @ApiModelProperty("兑奖码状态，0 未使用 1 已使用")
    private Integer rewardCodeStatus;

    /**
     * 兑奖用户id
     */
    @Excel(name = "兑奖用户id")
    @ApiModelProperty("兑奖用户id")
    private String rewardUserId;

    /**
     * 兑奖人姓名
     */
    @Excel(name = "兑奖人姓名")
    @ApiModelProperty("兑奖人姓名")
    private String rewardUser;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskFissionId", getTaskFissionId())
                .append("rewardCode", getRewardCode())
                .append("rewardCodeStatus", getRewardCodeStatus())
                .append("rewardUserId", getRewardUserId())
                .append("rewardUser", getRewardUser())
                .append("createTime", getCreateTime())
                .toString();
    }
}
