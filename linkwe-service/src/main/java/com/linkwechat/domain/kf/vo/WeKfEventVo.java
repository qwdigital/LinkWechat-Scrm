package com.linkwechat.domain.kf.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.kf.WeKfUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author leejoker
 * @version 1.0
 * @date 2022/1/24 19:13
 */
@ApiModel
@Data
public class WeKfEventVo {
    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("咨询客户id")
    private String customerId;

    @ApiModelProperty("咨询客户")
    private String customer;

    @ApiModelProperty("咨询客户头像")
    private String customerAvatar;

    @ApiModelProperty("是否为企业客户: 0-否 1-是")
    private Integer corpCustomer;

    @ApiModelProperty("客服id")
    private String kfId;

    @ApiModelProperty("客服名称")
    private String name;

    @ApiModelProperty("客服头像")
    private String avatar;

    @ApiModelProperty("接待方式: 1-人工客服 2-智能助手")
    private Integer receptionType;

    @ApiModelProperty("接待员工")
    private WeKfUser user;

    @ApiModelProperty("咨询开始时间")
     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    @ApiModelProperty("咨询结束时间")
     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty("咨询时长")
    private String costTime;
}
