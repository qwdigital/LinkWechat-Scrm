package com.linkwechat.domain.qr.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 拉新活码员工新增入参
 * @date 2023/03/07 13:49
 **/
@ApiModel
@Data
public class WeLxQrUserInfoQuery {

    @ApiModelProperty("员工类型 1-员工 2-部门 3-岗位")
    private Integer scopeType;

    @ApiModelProperty("部门id列表")
    private List<Long> partys;

    @ApiModelProperty("员工id列表")
    private List<String> userIds;

    @ApiModelProperty("岗位列表")
    private List<String> positions;
}
