package com.linkwechat.wecom.domain.query.qr;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 活码员工排期入参
 * @date 2021/11/7 13:58
 **/
@ApiModel
@Data
public class WeQrUserInfoQuery {

    @ApiModelProperty(value = "排期分组id(修改接口时传)")
    private String scopeId;

    @ApiModelProperty("排班员工类型 0 默认排期 1 自定义排期")
    private Integer type;

    @ApiModelProperty("部门id列表")
    private List<Integer> partys;

    @ApiModelProperty("员工id列表")
    private List<String> userIds;

    @ApiModelProperty("工作周期")
    private List<Integer> workCycle;

    @ApiModelProperty("开始时间")
    private String beginTime;

    @ApiModelProperty("结束时间")
    private String endTime;
}
