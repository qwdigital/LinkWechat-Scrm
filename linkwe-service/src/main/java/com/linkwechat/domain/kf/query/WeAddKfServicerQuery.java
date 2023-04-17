package com.linkwechat.domain.kf.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author danmo
 * @description 新增客服入参
 * @date 2022/1/18 21:48
 **/
@ApiModel
@Data
public class WeAddKfServicerQuery {

    @NotNull(message = "客服ID不能为空")
    private Long id;

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

    @ApiModelProperty("客户超时未回复提醒: 1-开启 2-关闭")
    private Integer timeOutNotice;

    @ApiModelProperty("客户超时时间")
    private Integer timeOut;

    @ApiModelProperty("客户超时时间类型 1-分钟 2-小时")
    private Integer timeOutType;

    @ApiModelProperty("客户超时未回复提醒内容")
    private String timeOutContent;


    @ApiModelProperty("客服超时未回复提醒: 1-开启 2-关闭")
    private Integer kfTimeOutNotice;

    @ApiModelProperty("客服超时时间")
    private Integer kfTimeOut;

    @ApiModelProperty("客户超时时间类型 1-分钟 2-小时")
    private Integer kfTimeOutType;


    @ApiModelProperty("自动结束提醒: 1-开启 2-关闭")
    private Integer endNotice;

    @ApiModelProperty("自动结束时间")
    private Integer endNoticeTime;

    @ApiModelProperty("自动结束时间类型 1-分钟 2-小时")
    private Integer endTimeType;

    @ApiModelProperty("自动结束提醒内容类型 1-会话质量评价 2-会话结束语")
    private Integer endContentType;

    @ApiModelProperty("自动结束提醒内容")
    private String endContent;

    @Size(message = "员工列表超出限制", max = 100)
    @ApiModelProperty("员工列表")
    private List<String> userIdList;

    @Size(message = "部门列表超出限制", max = 20)
    @ApiModelProperty("接待人员部门id列表")
    private List<Long> departmentIdList;
}
