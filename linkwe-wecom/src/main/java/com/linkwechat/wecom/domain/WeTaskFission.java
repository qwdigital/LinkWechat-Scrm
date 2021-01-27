package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

/**
 * 任务宝主对象 we_task_fission
 *
 * @author leejoker <1056650571@qq.com>
 * @date 2021-01-27
 */
@Data
public class WeTaskFission extends BaseEntity {
    private static final long serialVersionUID = -3145843701063325455L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 任务活动名称
     */
    @Excel(name = "任务活动名称")
    private String taskName;

    /**
     * 裂变引导语
     */
    @Excel(name = "裂变引导语")
    private String fissInfo;

    /**
     * 裂变客户数量
     */
    @Excel(name = "裂变客户数量")
    private Long fissNum;

    /**
     * 活动开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "活动开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 活动结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "活动结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date overTime;

    /**
     * 发起成员
     */
    @TableField(exist = false)
    private List<WeTaskFissionStaff> taskFissionStaffs;

    /**
     * 客户标签id列表，当为全部时保存为all
     */
    @Excel(name = "客户标签id列表，当为全部时保存为all")
    private String customerTagId;

    /**
     * 客户标签名称列表，为all是可为空
     */
    @Excel(name = "客户标签名称列表，为all是可为空")
    private String customerTag;

    /**
     * 海报id
     */
    @Excel(name = "海报id")
    private Long postersId;

    /**
     * 裂变海报路径
     */
    @Excel(name = "裂变海报路径")
    private String postersUrl;

    /**
     * 任务裂变目标员工
     */
    @Excel(name = "任务裂变目标员工")
    private String fissStaffId;

    /**
     * 任务裂变目标员工姓名
     */
    @Excel(name = "任务裂变目标员工姓名")
    private String fissStaff;

    /**
     * 任务裂变目标员工二维码
     */
    @Excel(name = "任务裂变目标员工二维码")
    private String fissStaffQrcode;

    /**
     * 兑奖链接
     */
    @Excel(name = "兑奖链接")
    private String rewardUrl;

    /**
     * 兑奖链接图片
     */
    @Excel(name = "兑奖链接图片")
    private String rewardImageUrl;

    /**
     * 兑奖规则
     */
    @Excel(name = "兑奖规则")
    private String rewardRule;

    /**
     * 任务裂变活动状态，1 进行中 2 已结束
     */
    @Excel(name = "任务裂变活动状态，1 进行中 2 已结束")
    private Integer fissStatus;

    /**
     * 新客欢迎语
     */
    @Excel(name = "新客欢迎语")
    private String welcomeMsg;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskName", getTaskName())
                .append("fissInfo", getFissInfo())
                .append("fissNum", getFissNum())
                .append("startTime", getStartTime())
                .append("overTime", getOverTime())
                .append("customerTagId", getCustomerTagId())
                .append("customerTag", getCustomerTag())
                .append("postersId", getPostersId())
                .append("postersUrl", getPostersUrl())
                .append("fissStaffId", getFissStaffId())
                .append("fissStaff", getFissStaff())
                .append("fissStaffQrcode", getFissStaffQrcode())
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
