package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * 任务宝主对象 we_task_fission
 *
 * @author leejoker <1056650571@qq.com>
 * @date 2021-01-27
 */
@ApiModel
@Data
public class WeTaskFission extends BaseEntity {
    private static final long serialVersionUID = -3145843701063325455L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 任务类型：1 任务宝 2 群裂变
     */
    @ApiModelProperty(value = "任务类型：1 任务宝 2 群裂变")
    private Integer fissionType;

    /**
     * 任务活动名称
     */
    @Excel(name = "任务活动名称")
    @ApiModelProperty(value = "任务活动名称")
    private String taskName;

    /**
     * 裂变引导语
     */
    @Excel(name = "裂变引导语")
    @ApiModelProperty(value = "裂变引导语")
    private String fissInfo;

    /**
     * 裂变客户数量
     */
    @Excel(name = "裂变客户数量")
    @ApiModelProperty(value = "裂变客户数量")
    private Long fissNum;

    /**
     * 活动开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "活动开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "活动开始时间")
    private Date startTime;

    /**
     * 活动结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "活动结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "活动结束时间")
    private Date overTime;

    /**
     * 发起成员
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "发起成员")
    private List<WeTaskFissionStaff> taskFissionStaffs;

    /**
     * 客户群
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "客户群")
    private List<WeGroup> taskFissionWeGroups;

    /**
     * 客户标签id列表，当为全部时保存为all
     */
    @Excel(name = "客户标签id列表，当为全部时保存为all")
    @ApiModelProperty(value = "客户标签id列表，当为全部时保存为all")
    private String customerTagId;

    /**
     * 客户标签名称列表，为all是可为空
     */
    @Excel(name = "客户标签名称列表，为all是可为空")
    @ApiModelProperty(value = "客户标签名称列表，为all是可为空")
    private String customerTag;

    /**
     * 海报id
     */
    @Excel(name = "海报id")
    @ApiModelProperty(value = "海报id")
    private Long postersId;

    /**
     * 裂变海报路径
     */
    @Excel(name = "裂变海报路径")
    @ApiModelProperty(value = "裂变海报路径")
    private String postersUrl;

    /**
     * 任务裂变目标员工
     */
    @Excel(name = "任务裂变目标id")
    @NotBlank(message = "目标员工或群活码不能为空")
    @ApiModelProperty(value = "任务裂变目标id, 目标员工或者群活码id")
    private String fissionTargetId;

    /**
     * 任务裂变目标员工姓名
     */
    @Excel(name = "任务裂变目标")
    @ApiModelProperty(value = "任务裂变目标, 目标员工名称或者群活码二维码地址")
    private String fissionTarget;

    /**
     * 任务裂变目标员工二维码
     */
    @Excel(name = "任务裂变目标二维码")
    @ApiModelProperty(value = "任务裂变目标二维码")
    private String fissQrcode;

    /**
     * 兑奖链接
     */
    @Excel(name = "兑奖链接")
    @ApiModelProperty(value = "兑奖链接")
    private String rewardUrl;

    /**
     * 兑奖链接图片
     */
    @Excel(name = "兑奖链接图片")
    @ApiModelProperty(value = "兑奖链接图片")
    private String rewardImageUrl;

    /**
     * 兑奖规则
     */
    @Excel(name = "兑奖规则")
    @ApiModelProperty(value = "兑奖规则")
    private String rewardRule;

    /**
     * 任务裂变活动状态，1 进行中 2 已结束
     */
    @Excel(name = "任务裂变活动状态，1 进行中 2 已结束")
    @ApiModelProperty(value = "任务裂变活动状态，-1-发送失败 0-待发送 1-进行中 2-已结束")
    private Integer fissStatus;

    /**
     * 新客欢迎语
     */
    @Excel(name = "新客欢迎语")
    @ApiModelProperty(value = "新客欢迎语")
    private String welcomeMsg;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("fissionType", getFissionType())
                .append("taskName", getTaskName())
                .append("fissInfo", getFissInfo())
                .append("fissNum", getFissNum())
                .append("startTime", getStartTime())
                .append("overTime", getOverTime())
                .append("customerTagId", getCustomerTagId())
                .append("customerTag", getCustomerTag())
                .append("postersId", getPostersId())
                .append("postersUrl", getPostersUrl())
                .append("fissionTargetId", getFissionTargetId())
                .append("fissionTarget", getFissionTarget())
                .append("fissQrcode", getFissQrcode())
                .append("rewardUrl", getRewardUrl())
                .append("rewardImageUrl", getRewardImageUrl())
                .append("rewardRule", getRewardRule())
                .append("fissStatus", getFissStatus())
                .append("welcomeMsg", getWelcomeMsg())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
