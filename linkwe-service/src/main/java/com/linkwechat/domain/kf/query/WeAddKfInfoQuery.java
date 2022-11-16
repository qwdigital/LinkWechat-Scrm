package com.linkwechat.domain.kf.query;

import com.linkwechat.domain.kf.WeKfUser;
import com.linkwechat.domain.kf.WeKfWelcomeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author danmo
 * @description 新增客服入参
 * @date 2022/1/18 21:48
 **/
@ApiModel
@Data
public class WeAddKfInfoQuery {

    private Long id;

    @NotBlank(message = "客服名称不能为空")
    @ApiModelProperty("客服名称")
    private String name;

    @NotBlank(message = "客服头像不能为空")
    @ApiModelProperty("客服头像")
    private String avatar;

    @ApiModelProperty("接待方式: 1-人工客服 2-智能助手")
    private Integer receptionType;

    @ApiModelProperty("是否分时段: 1-否 2-是")
    private Integer splitTime;

    @ApiModelProperty("分配方式: 1-轮流 2-空闲")
    private Integer allocationWay;

    @ApiModelProperty("是否有限分配: 1-否 2-是")
    private Integer isPriority;

    @ApiModelProperty("接待限制")
    private Integer receiveLimit;

    @ApiModelProperty("排队提醒: 1-开启 2-关闭")
    private Integer queueNotice;

    @ApiModelProperty("排队提醒内容")
    private String queueNoticeContent;

    @ApiModelProperty("超时未回复提醒: 1-开启 2-关闭")
    private Integer timeOutNotice;

    @ApiModelProperty("超时时间")
    private Integer timeOut;

    @ApiModelProperty("超时时间类型 1-分钟 2-小时")
    private Integer timeOutType;

    @ApiModelProperty("超时未回复提醒内容")
    private String timeOutContent;

    @ApiModelProperty("自动结束提醒: 1-开启 2-关闭")
    private Integer endNotice;

    @ApiModelProperty("自动结束时间")
    private Integer endNoticeTime;

    @ApiModelProperty("自动结束时间类型 1-分钟 2-小时")
    private Integer endTimeType;

    @ApiModelProperty("自动结束提醒内容")
    private String endContent;

    @NotEmpty(message = "接待人员不能为空")
    @Size(message = "员工列表超出限制", max = 500, min = 1)
    @ApiModelProperty("员工列表")
    private List<WeKfUser> userIdList;

    @NotEmpty(message = "欢迎语不能为空")
    @ApiModelProperty("欢迎语")
    private List<WeKfWelcomeInfo> welcome;
}
