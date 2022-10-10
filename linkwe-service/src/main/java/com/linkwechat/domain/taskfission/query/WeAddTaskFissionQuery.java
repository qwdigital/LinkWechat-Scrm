package com.linkwechat.domain.taskfission.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.WeTaskFissionStaff;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@ApiModel
@Data
public class WeAddTaskFissionQuery {

    @ApiModelProperty("主键ID(修改时传参)")
    private Long id;

    /**
     * 活动类型，1 任务宝 2 群裂变
     */
    @ApiModelProperty(value = "活动类型，1 任务宝 2 群裂变")
    private Integer fissionType;


    /**
     * 任务活动名称
     */
    @ApiModelProperty(value = "任务活动名称")
    private String taskName;


    /**
     * 裂变引导语
     */
    @ApiModelProperty(value = "裂变引导语")
    private String fissInfo;


    /**
     * 裂变客户数量
     */
    @ApiModelProperty(value = "裂变客户数量")
    private Integer fissNum;


    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;


    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date overTime;


    /**
     * 客户标签id列表，当为全部时保存为all
     */
    @ApiModelProperty(value = "客户标签id列表，当为全部时保存为all")
    private String customerTagId;


    /**
     * 客户标签名称列表，为all是可为空
     */
    @ApiModelProperty(value = "客户标签名称列表，为all是可为空")
    private String customerTag;


    /**
     * 海报id
     */
    @ApiModelProperty(value = "海报id")
    private Long postersId;


    /**
     * 裂变海报路径
     */
    @ApiModelProperty(value = "裂变海报路径")
    private String postersUrl;


    /**
     * 任务裂变目标员工/群裂变id
     */
    @ApiModelProperty(value = "任务裂变目标员工/群裂变id")
    private String fissionTargetId;


    /**
     * 任务裂变目标员工姓名/群裂变二维码地址
     */
    @ApiModelProperty(value = "任务裂变目标员工姓名/群裂变二维码地址")
    @TableField("fission_target")
    private String fissionTarget;


    /**
     * 任务裂变目标二维码
     */
    @ApiModelProperty(value = "任务裂变目标二维码")
    private String fissQrcode;


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


    /**
     * 任务裂变活动状态，0 未开始 1 进行中 2 已结束
     */
    @ApiModelProperty(value = "任务裂变活动状态，0 未开始 1 进行中 2 已结束")
    private Integer fissStatus;


    /**
     * 新客欢迎语
     */
    @ApiModelProperty(value = "新客欢迎语")
    private String welcomeMsg;

    /**
     * 发起成员
     */
    @ApiModelProperty(value = "发起成员")
    private List<WeTaskFissionStaff> taskFissionStaffs;

    /**
     * 客户群
     */
    @ApiModelProperty(value = "客户群")
    private List<WeGroup> taskFissionWeGroups;
}
