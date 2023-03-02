package com.linkwechat.domain.kf.vo;

import com.linkwechat.domain.kf.WeKfUser;
import com.linkwechat.domain.kf.WeKfWelcomeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 客服详情
 * @date 2022/1/18 21:48
 **/
@ApiModel
@Data
public class WeKfInfoVo {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("企业ID")
    private String corpId;

    @ApiModelProperty("客服账号Id")
    private String openKfId;

    @ApiModelProperty("客服名称")
    private String name;

    @ApiModelProperty("客服头像")
    private String avatar;

    @ApiModelProperty("接待方式: 1-人工客服 2-智能助手")
    private Integer receptionType;

    @ApiModelProperty("是否分时段: 1-否 2-是")
    private Integer splitTime;

    @ApiModelProperty("分配方式: 1-轮流 2-空闲")
    private Integer allocationWay;

    @ApiModelProperty("是否优先分配: 1-否 2-是")
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

    @ApiModelProperty("客服超时未回复提醒: 1-开启 2-关闭")
    private Integer kfTimeOutNotice;

    @ApiModelProperty("客户超时时间类型 1-分钟 2-小时")
    private Integer kfTimeOutType;

    @ApiModelProperty("客服超时时间")
    private Integer kfTimeOut;

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

    @ApiModelProperty("员工列表")
    private List<WeKfUser> userIdList;

    @ApiModelProperty("欢迎语")
    private List<WeKfWelcomeInfo> welcome;
}
